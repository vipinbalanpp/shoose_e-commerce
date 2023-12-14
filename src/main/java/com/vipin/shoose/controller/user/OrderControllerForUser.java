package com.vipin.shoose.controller.user;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.vipin.shoose.dto.*;
import com.vipin.shoose.model.*;
import com.vipin.shoose.repository.CartRepository;
import com.vipin.shoose.repository.OrderRepository;
import com.vipin.shoose.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/user")
public class OrderControllerForUser {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    @Autowired
    CartService cartService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CouponService couponService;

    @PostMapping("/add-address-from-checkout")
    public  String addAddress(AddressDto addressDto){
        addressService.save(addressDto);
        return "redirect:/user/check-out";
    }
    @GetMapping("/place-order")
    public String placeOrderGet(Model model)  {
        return "/user/order-success";
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestParam("addressId") Long addressId,
                             @RequestParam("totalAmount") Float totalAmount,
                             @RequestParam("paymentMethod")String paymentMethod,
                              @RequestParam("discountCode")String discountCode,
                             @RequestParam("totalDiscount")Float totalDiscount,
                             RedirectAttributes redirectAttributes)  {
        Long orderId=orderService.placeOrder(addressId, totalAmount,paymentMethod,discountCode,totalDiscount);
       redirectAttributes.addFlashAttribute("orderId",orderId);
        return "redirect:/user/place-order";
    }
    @GetMapping("/order-details/{orderId}")
    public String productDetails( @PathVariable("orderId") Long orderId,
                                  Model model){
       OrderDto orderDto=orderService.getOrderDetails(orderId);
        System.out.println(orderDto.getOrderId());
       List<SelectedProducts>products=orderService.setProductsToOrderDetailsPage(orderRepository.findByOrderId(orderId).getProducts());
       model.addAttribute("order",orderDto);
       model.addAttribute("products",products);
        return "/user/order-details";
    }
    @PostMapping("/order-details/{orderId}")
    public String trackOrder(@PathVariable("orderId") Long orderId, Model model) {
        return "redirect:/user/order-details/"+orderId;
    }
    @GetMapping("/order-list")
    public String orderList(Model model){
        model.addAttribute("orders", userService.getCurrentUser().getOrders());
        return "/user/order-list";
    }
    @PostMapping("/cancel-order")
    public String cancelOrder(@RequestParam("orderId") Long orderId,
                              RedirectAttributes redirectAttributes){
        System.out.println("fod"+orderId);
        orderService.cancelOrder(orderId);
        redirectAttributes.addFlashAttribute("cancel","Order Cancelled Successfully");
        return "redirect:/user/order-details/"+orderId;
    }
    @PostMapping("/createOrder")
    @ResponseBody
    public ResponseEntity<String> createOrder (@RequestParam("amount")String amount) throws Exception{
        float amt= Float.parseFloat((amount));
        var client=new RazorpayClient("rzp_test_XOMufWarHqGN9i","hUMSB4Y98NuC3XJ2W25PN2q3");
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",amt*100);
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", "txn_2345433");
        Order order=client.orders.create(orderRequest);
        return new ResponseEntity<>(order.toString(), HttpStatus.OK);
    }
    @GetMapping("/isAbleForReturn")
    public ResponseEntity<Boolean> isAbleForReturn(@RequestParam("orderId") Long orderId){
        return new ResponseEntity<>(orderService.isAbleForReturn(orderId),HttpStatus.OK);
    }
    @PostMapping("/returnRequest")
    public String returnRequest(@RequestParam("orderId")Long orderId){
        orderService.sendReturnRequest(orderId);
        return "redirect:/user/order-details/"+orderId;
    }
    @GetMapping("/checkCouponExists")
    public ResponseEntity<Boolean> couponExists(@RequestParam("couponCode")String couponCode){
        System.out.println("coupon method called");
        return new ResponseEntity<>(couponService.checkCouponExists(couponCode),HttpStatus.OK);
    }
    @GetMapping("/applyCoupon")
    public ResponseEntity<CouponResponse>applyCoupon(@RequestParam("couponCode")String couponCode,
                                       @RequestParam("totalAmount")float totalAmount){
        float d=couponService.getDisCountAmount(couponCode);
        CouponResponse couponResponse=new CouponResponse();
        if(couponService.getMinumumPurchaseAmount(couponCode)<totalAmount){
            Float t=couponService.applyCoupon(couponCode,totalAmount);
            couponResponse.setDiscountAmount(d);
            couponResponse.setTotalAmount(t);
            couponResponse.setAbleToApply(true);
            System.out.println(couponResponse.isAbleToApply());
            return new ResponseEntity<>(couponResponse,HttpStatus.OK);
        }
            couponResponse.setAbleToApply(false);
        return new ResponseEntity<>(couponResponse,HttpStatus.OK);
    }
    @GetMapping("/getOrderStatus")
    @ResponseBody
    public ResponseEntity<OrderDto> updateOrderStatus(@RequestParam("orderId") Long orderId) {
        String status = String.valueOf(orderService.getStatus(orderId));
        List<String>statuses=orderService.getOrderStatusesWithDate(orderId);
        OrderDto orderDto=new OrderDto();
        orderDto.setOrderStatus(status);
        orderDto.setStatuses(statuses);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
    @PostMapping("/add-addressFromCheckout")
    public  String addAddressFromCheckout(AddressDto addressDto){
        addressService.save(addressDto);
        return "redirect:/user/check-out";
    }
    @GetMapping("/getAddressesFromCheckout")
    public ResponseEntity<List<Address>> getAddresses(){
        return new ResponseEntity<>(userService.getAddresses(userService.getCurrentUser()),HttpStatus.OK);
    }




}
