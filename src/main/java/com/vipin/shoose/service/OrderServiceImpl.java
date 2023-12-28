package com.vipin.shoose.service;

import com.vipin.shoose.dto.OrderDto;
import com.vipin.shoose.dto.SelectedProducts;
import com.vipin.shoose.exception.CustomException;
import com.vipin.shoose.model.*;
import com.vipin.shoose.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{
    @Autowired
    UserService userService;
    @Autowired
    ShippingAddressService shippingAddressService;
   @Autowired
   CartService cartService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    VariantService variantService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CouponService couponService;
    @Override
    public List<OrderStatusEnum> getStatusesToUpdate(Long orderId) {
        try {
            OrderInfo order=orderRepository.findByOrderId(orderId);
            List<OrderStatusEnum> statuses = new ArrayList<>(Arrays.asList(OrderStatusEnum.values()));
            return statuses;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public Long placeOrder(Long addressId, Float totalAmount, String paymentMethod,String couponCode,Float totalDiscount) {
        try {
            Cart cart=cartRepository.findByUserId(userService.getCurrentUser().getUserId());
            OrderInfo order=new OrderInfo();
            User user=userService.getCurrentUser();
            order.setUser(user);
            if(!Objects.equals(couponCode, "not-applied")){
                couponService.couponApplied(couponCode,userService.getCurrentUser());
            }
            order.setTotalAmount(totalAmount);
            order.setDiscountAmount(totalDiscount);
            order.setAmountPaid(totalAmount-totalDiscount);
            order.setOrderedDate(LocalDate.now());
            if(Objects.equals(paymentMethod, "razorpay")){
                order.setPaymentMethode(PaymentMethod.RAZORPAY);
            }else if(Objects.equals(paymentMethod, "cod")){
                order.setPaymentMethode(PaymentMethod.CASH_ON_DELIVERY);
            }else if(Objects.equals(paymentMethod, "wallet")){
                order.setPaymentMethode(PaymentMethod.WALLET);
                userService.payfromWallet(Double.valueOf(totalAmount));
            }
            order.setStatus(OrderStatusEnum.PENDING, LocalDate.now());
            order.setShippingAddress(shippingAddressService.createOrderShippingAddress(Long.valueOf(addressId)));
            order.setCurrentStatus(OrderStatusEnum.PENDING);
            System.out.println(order.getCurrentStatus());
            Map<Variant,Long>cartProducts=cart.getProducts();
            Map<Variant,Long>orderProducts=new HashMap<>(cartProducts);
            variantService.manageStockForOrder(orderProducts);
            setProductsForOrder(orderProducts,order);;
            cart.getProducts().clear();
            orderRepository.save(order);
            cartRepository.save(cart);
            return order.getOrderId();
        }catch (Exception e){

            throw  new RuntimeException();
        }
}

    private void setProductsForOrder(Map<Variant, Long> orderProducts, OrderInfo order) {
        try {
            for(Variant variant:orderProducts.keySet()){
                order.setProducts(variant,orderProducts.get(variant));
            }
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public OrderDto getOrderDetails(Long orderId) {
        try {
            OrderDto orderDto=new OrderDto();
            OrderInfo orderInfo=orderRepository.findByOrderId(orderId);
            orderDto.setOrderId(orderId);
            for(LocalDate date:orderInfo.getStatus().values()){
                System.out.println(date);
            }
            for(OrderStatusEnum date:orderInfo.getStatus().keySet()){
                System.out.println(date);
            }
            orderDto.setOrderedDate(orderInfo.getOrderedDate());
            orderDto.setCustomerName(orderInfo.getShippingAddress().getFullName());
            orderDto.setTotalAmount(orderInfo.getTotalAmount());
            orderDto.setTotalDiscount(orderInfo.getDiscountAmount());
            orderDto.setAmountPaid(orderInfo.getAmountPaid());
            if(orderInfo.getPaymentMethode()==PaymentMethod.CASH_ON_DELIVERY){
                orderDto.setPaymentMethode("Cash on Delivery");
            }else if(orderInfo.getPaymentMethode()==PaymentMethod.RAZORPAY){
                orderDto.setPaymentMethode("Paid using Razorpay");
            }else {
                orderDto.setPaymentMethode("Paid Using Shoose Wallet");
            }
            orderDto.setOrderStatus(String.valueOf(orderInfo.getCurrentStatus()));
            return orderDto;
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public List<SelectedProducts> setProductsToOrderDetailsPage(Map<Variant, Long> products) {
        try {
            List<SelectedProducts>selectedProducts=new ArrayList<>();
            for(Variant variant:products.keySet()){
                SelectedProducts selectedProduct=new SelectedProducts();
                selectedProduct.setProductName(productRepository.findByProductId(variant.getProduct().getProductId()).getProductName());
                selectedProduct.setBrand(productRepository.findByProductId(variant.getProduct().getProductId()).getBrand());
                selectedProduct.setColor(variant.getColor());
                selectedProduct.setSize(variant.getSize());
                selectedProduct.setActualPrice(productRepository.findByProductId(variant.getProduct().getProductId()).getActualPrice());
                selectedProduct.setImage(productImageService.getProductImage(variant));
                selectedProduct.setQuantity(products.get(variant));
                selectedProducts.add(selectedProduct);
            }return selectedProducts;
        }catch (Exception e){
            throw new CustomException("An error Occurred");
        }

    }
    @Override
    public Object getAllOrders() {
        try {
            return orderRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public void cancelOrder(Long orderId) {
        try {
            OrderInfo orderInfo=orderRepository.findByOrderId(orderId);
            variantService.updateStockonCancellingOrder(orderId);
            orderInfo.setCurrentStatus(OrderStatusEnum.CANCELLED);
            orderInfo.setStatus(OrderStatusEnum.CANCELLED,LocalDate.now());
            orderRepository.save(orderInfo);
            if(orderInfo.getPaymentMethode().equals(PaymentMethod.WALLET)||orderInfo.getPaymentMethode().equals(PaymentMethod.RAZORPAY)){
                userService.cancelOrderMoneyReturn(Double.valueOf(orderInfo.getAmountPaid()));
            }
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public void updateOrderStatus(Long orderId, String newStatus) {
        try {
            OrderInfo order=orderRepository.findByOrderId(orderId);
            order.setStatus(OrderStatusEnum.valueOf(newStatus),LocalDate.now());
            order.setCurrentStatus(OrderStatusEnum.valueOf(newStatus));
            orderRepository.save(order);
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public OrderStatusEnum getStatus(Long orderId) {
        try {
            return orderRepository.findByOrderId(orderId).getCurrentStatus();
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public List<String> getOrderStatusesWithDate(Long orderId) {
        try {
            OrderInfo order=orderRepository.findByOrderId(orderId);
            List<String>statuses=new ArrayList<>();
            if(order.getCurrentStatus()==OrderStatusEnum.PENDING){
                return null;
            }else if(order.getCurrentStatus()==OrderStatusEnum.PROCESSING){
                String processing="Order Processing";
                statuses.add(processing);
                return statuses;
            }else if(order.getCurrentStatus()==OrderStatusEnum.SHIPPED){
                String s="Order Processing";
                statuses.add(s);
                String shipped="Order Shipped on "+order.getStatus().get(OrderStatusEnum.SHIPPED);
                statuses.add(shipped);
                return statuses;
            }else if(order.getCurrentStatus()==OrderStatusEnum.OUT_FOR_DELIVERY){
                String s="Order Processing";
                statuses.add(s);
                String shipped="Order Shipped on "+order.getStatus().get(OrderStatusEnum.SHIPPED);
                statuses.add(shipped);
                String outForDelivery="Order is out for delivery from "+order.getStatus().get(OrderStatusEnum.OUT_FOR_DELIVERY);
                statuses.add(outForDelivery);
                return statuses;
            }else if(order.getCurrentStatus()==OrderStatusEnum.DELIVERED||order.getCurrentStatus()==OrderStatusEnum.RETURN_REQUESTED||order.getCurrentStatus()==OrderStatusEnum.RETURNED){
                String s="Order Processing";
                statuses.add(s);
                String shipped="Order Shipped on "+order.getStatus().get(OrderStatusEnum.SHIPPED);
                statuses.add(shipped);
                String outForDelivery="Order is out for delivery from "+order.getStatus().get(OrderStatusEnum.OUT_FOR_DELIVERY);
                statuses.add(outForDelivery);
                String delivered="Order Delivered on "+order.getStatus().get(OrderStatusEnum.DELIVERED);
                statuses.add(delivered);
                return statuses;
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public int getSoldProducts() {
        try {
            List<OrderInfo>orders=orderRepository.findAll();
            int count=0;
            for(OrderInfo orderInfo:orders){
                if(orderInfo.getCurrentStatus()==OrderStatusEnum.DELIVERED){
                    count+= orderInfo.getProducts().size();
                }
            }return count;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public double getTotalSales() {
        try {
            List<OrderInfo>orders=orderRepository.findAll();
            double totalSales=0.0;
            for(OrderInfo orderInfo:orders){
                if(orderInfo.getCurrentStatus()==OrderStatusEnum.DELIVERED){
                    totalSales+=orderInfo.getAmountPaid();
                }
            }return totalSales;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public Map<String, Double> getWeeklySales() {
        try {
            Map<String,Double>weeklySales=new LinkedHashMap<>();
            Calendar calendar=Calendar.getInstance();
            for(int i=1;i<=7;i++){
                Date weekEndDate = calendar.getTime();
                calendar.add(Calendar.WEEK_OF_YEAR,-1);
                Date weekStartDate=calendar.getTime();
                LocalDate weekStart=weekStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate weekEnd = weekEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                List<OrderInfo> list= orderRepository.getWeeklyFromStartToEnd(weekStart,weekEnd);
                double totalOrderAmount = list.stream().mapToDouble(OrderInfo::getAmountPaid).sum();
                weeklySales.put(weekStart+"_"+weekEnd,totalOrderAmount);
            }return weeklySales;
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public Map<String, Long> getWeeklyCount() {
        try {
            Map<String,Long>weeklyCount=new LinkedHashMap<>();
            Calendar calendar=Calendar.getInstance();
            for(int i=1;i<=7;i++){
                Date weekEndDate = calendar.getTime();
                calendar.add(Calendar.WEEK_OF_YEAR,-1);
                Date weekStartDate=calendar.getTime();
                LocalDate weekStart=weekStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate weekEnd = weekEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                List<OrderInfo> list= orderRepository.getWeeklyFromStartToEnd(weekStart,weekEnd);
                Long count=0L;
                for(OrderInfo order:list){
                    count+=order.getProducts().size();
                }
                weeklyCount.put(weekStart+"_"+weekEnd,count);
            }return weeklyCount;
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public Map<String, Double> getDailySales() {
        try {
            Map<String, Double> dailySale = new LinkedHashMap<>();
            double orderTotalAmount ;
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < 7; i++) {
                Date date = calendar.getTime();
                LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                List<OrderInfo> list = orderRepository.getDailyFromCurrentDay(currentDate);
                orderTotalAmount = list.stream().mapToDouble(OrderInfo::getAmountPaid).sum();
                dailySale.put(currentDate.toString(), orderTotalAmount);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }
            return dailySale;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }


    }

    @Override
    public Map<String, Long> getDailyCount() {
        try {
            Map<String, Long> dailySale = new LinkedHashMap<>();
            Calendar calendar = Calendar.getInstance();
            Long count=0L;
            for (int i = 0; i < 7; i++) {
                Date date = calendar.getTime();
                LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                List<OrderInfo> list = orderRepository.getDailyFromCurrentDay(currentDate);
                for(OrderInfo order:list){
                    count+=order.getProducts().size();
                }
                dailySale.put(currentDate.toString(),count);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }
            return dailySale;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public Map<String, Double> getMonthlySales() {
        try {
            Map<String, Double> monthlySales = new LinkedHashMap<>();
            YearMonth currentYearMonth = YearMonth.now();
            Month currentMonth = currentYearMonth.getMonth();
            for (int i = currentMonth.getValue() - 1; i >= 0; i--) {
                YearMonth targetYearMonth = currentYearMonth.minusMonths(i);
                Month targetMonth = targetYearMonth.getMonth();
                LocalDate monthStart = targetYearMonth.atDay(1);
                LocalDate monthEnd = targetYearMonth.atEndOfMonth();
                List<OrderInfo> list = orderRepository.getMonthlyFromStartToEnd(monthStart, monthEnd);
                double totalOrderAmount = list.stream().mapToDouble(OrderInfo::getAmountPaid).sum();
                monthlySales.put(targetMonth.toString(), totalOrderAmount);
            }
            return monthlySales;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }
    @Override
    public Map<String, Long> getMonthlySalesCount() {
        try {
            Map<String, Long> monthlySales = new LinkedHashMap<>();
            YearMonth currentYearMonth = YearMonth.now();
            Month currentMonth = currentYearMonth.getMonth();
            Long count=0L;
            for (int i = currentMonth.getValue() - 1; i >= 0; i--) {
                YearMonth targetYearMonth = currentYearMonth.minusMonths(i);
                Month targetMonth = targetYearMonth.getMonth();
                LocalDate monthStart = targetYearMonth.atDay(1);
                LocalDate monthEnd = targetYearMonth.atEndOfMonth();
                List<OrderInfo> list = orderRepository.getMonthlyFromStartToEnd(monthStart, monthEnd);
                for(OrderInfo order:list){
                    count+=order.getProducts().size();
                }
                monthlySales.put(targetMonth.toString(), count);
            }
            return monthlySales;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public Map<String, Double> getYearlySales() {
        try {
            Map<String, Double> yearlySales = new LinkedHashMap<>();
            Year currentYear = Year.now();
            for (int i = 4; i >= 0; i--) {
                Year targetYear = currentYear.minusYears(i);
                LocalDate yearStart = targetYear.atDay(1);
                LocalDate yearEnd = targetYear.atMonth(Month.DECEMBER).atEndOfMonth();
                List<OrderInfo> list = orderRepository.getYearlyFromStartToEnd(yearStart, yearEnd);
                double totalOrderAmount = list.stream().mapToDouble(OrderInfo::getAmountPaid).sum();
                yearlySales.put(Integer.toString(targetYear.getValue()), totalOrderAmount);
            }
            return yearlySales;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public Map<String, Long> getYearlySalesCount() {
        try {
            Map<String, Long> yearlySales = new LinkedHashMap<>();
            Year currentYear = Year.now();
            Long count=0L;
            for (int i = 4; i >= 0; i--) {
                Year targetYear = currentYear.minusYears(i);
                LocalDate yearStart = targetYear.atDay(1);
                LocalDate yearEnd = targetYear.atMonth(Month.DECEMBER).atEndOfMonth();
                List<OrderInfo> list = orderRepository.getYearlyFromStartToEnd(yearStart, yearEnd);
                for(OrderInfo order:list){
                    count+=order.getProducts().size();
                }
                yearlySales.put(Integer.toString(targetYear.getValue()), count);
            }
            return yearlySales;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public byte[] salesReportDayWise(String startDate, String endDate) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);


            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                List<OrderInfo> ordersList = generateMonthlyReport(startDate, endDate);

                // Brand Name
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(250, 700);
                contentStream.showText("Sales Report");
                contentStream.endText();


                // Add table headers
                float yPosition = 575;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Customer Name");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Order Date");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Payment Method");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Order Total");
                contentStream.endText();

                // Move to the next line for the data
                yPosition -= 20;

                // Add table data
                for (OrderInfo orders : ordersList) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(orders.getShippingAddress().getFullName());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText(String.valueOf(orders.getOrderedDate()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(String.valueOf(orders.getPaymentMethode()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText(String.format("$%.2f", orders.getAmountPaid()));
                    contentStream.endText();

                    // Move to the next line for the next orders
                    yPosition -= 20;
                }
                contentStream.close();
                document.save(baos);
                return baos.toByteArray();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] monthlySalesReport() throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 PDDocument document = new PDDocument()) {

                PDPage page = new PDPage();
                document.addPage(page);


                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                    Map<String, Double> salesReport = getMonthlySales();

                    // Brand Name
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.newLineAtOffset(250, 700);
                    contentStream.showText("Sales Report");
                    contentStream.endText();


                    // Add table headers
                    float yPosition = 575;
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText("Month ");
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText("Order Total");
                    contentStream.endText();

                    // Move to the next line for the data
                    yPosition -= 20;

                    // Add table data
                    for (String keys : salesReport.keySet()) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.newLineAtOffset(50, yPosition);
                        contentStream.showText(keys);
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.newLineAtOffset(50, 0);
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.newLineAtOffset(50, 0);
                        contentStream.showText(String.format("$%.2f",salesReport.get(keys)));
                        contentStream.endText();

                        // Move to the next line for the next orders
                        yPosition -= 20;
                    }
                    contentStream.close();
                    document.save(baos);
                    return baos.toByteArray();
                }

            }
        }

    @Override
    public byte[] yearlySalesReport() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                Map<String, Double> salesReport = getYearlySales();
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(250, 700);
                contentStream.showText("Sales Report");
                contentStream.endText();

                float yPosition = 575;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Year ");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Order Total");
                contentStream.endText();

                // Move to the next line for the data
                yPosition -= 20;

                // Add table data
                for (String keys : salesReport.keySet()) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(keys);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText(String.format("$%.2f", salesReport.get(keys)));
                    contentStream.endText();

                    // Move to the next line for the next orders
                    yPosition -= 20;
                }
                contentStream.close();
                document.save(baos);
                return baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public Boolean isAbleForReturn(Long orderId) {
        try {
            OrderInfo order=orderRepository.findByOrderId(orderId);
            LocalDate deliveredDate= order.getStatus().get(OrderStatusEnum.DELIVERED);
            if(deliveredDate!=null){
                LocalDate currentTime=LocalDate.now();
                Period period=Period.between(deliveredDate,currentTime);
                return period.getDays() <=10;
            }return false;
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public void sendReturnRequest(Long orderId) {
        try {
            OrderInfo orderInfo=orderRepository.findByOrderId(orderId);
            orderInfo.setCurrentStatus(OrderStatusEnum.RETURN_REQUESTED);
            orderInfo.setStatus(OrderStatusEnum.RETURN_REQUESTED,LocalDate.now());
            orderRepository.save(orderInfo);
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public void confirmReturn(Long orderId) {
        try {
            OrderInfo orderInfo=orderRepository.findByOrderId(orderId);
            orderInfo.setCurrentStatus(OrderStatusEnum.RETURNED);
            orderInfo.setStatus(OrderStatusEnum.RETURNED,LocalDate.now());
            variantService.updateStockonCancellingOrder(orderId);
            userService.cancelOrderMoneyReturn(Double.valueOf(orderInfo.getAmountPaid()));
            orderRepository.save(orderInfo);
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    public List<OrderInfo> generateMonthlyReport(String stringStartDate, String stringEndDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(stringStartDate, formatter);
            LocalDate endDate = LocalDate.parse(stringEndDate, formatter);
            return orderRepository.monthlySalesReport(startDate, endDate);
        }catch (Exception e){
            throw new RuntimeException();
        }

    }


}











