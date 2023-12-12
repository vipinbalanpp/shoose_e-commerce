package com.vipin.shoose.service;

import com.vipin.shoose.dto.OrderDto;
import com.vipin.shoose.dto.SelectedProducts;
import com.vipin.shoose.model.OrderStatusEnum;
import com.vipin.shoose.model.Variant;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface OrderService {
    public List<OrderStatusEnum> getStatusesToUpdate(Long orderId);


    Long  placeOrder(Long addressId, Float totalAmount, String paymentMethod,String discountCode,Float totalDiscount);

   OrderDto getOrderDetails(Long orderId);

   List<SelectedProducts> setProductsToOrderDetailsPage(Map<Variant,Long>products);


    Object getAllOrders();

    void cancelOrder(Long orderId);

    void updateOrderStatus(Long orderId, String newStatus);

    OrderStatusEnum getStatus(Long orderId);

    List<String> getOrderStatusesWithDate(Long orderId);

    int getSoldProducts();

    double getTotalSales();

    Map<String, Double> getWeeklySales();

    Map<String, Long> getWeeklyCount();

    Map<String, Double> getDailySales();

    Map<String, Long> getDailyCount();

    Map<String, Double> getMonthlySales();

    Map<String, Long> getMonthlySalesCount();

    Map<String, Double> getYearlySales();

    Map<String, Long> getYearlySalesCount();

    byte[] salesReportDayWise(String startDate, String endDate);

 byte[] monthlySalesReport() throws IOException;

 byte[] yearlySalesReport() throws IOException;

    Boolean isAbleForReturn(Long orderId);

    void sendReturnRequest(Long orderId);

    void confirmReturn(Long orderId);
}
