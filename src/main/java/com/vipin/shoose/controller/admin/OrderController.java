package com.vipin.shoose.controller.admin;

import com.vipin.shoose.dto.OrderDto;
import com.vipin.shoose.dto.SelectedProducts;
import com.vipin.shoose.model.OrderStatusEnum;
import com.vipin.shoose.repository.OrderRepository;
import com.vipin.shoose.service.OrderService;
import kotlin.text.UStringsKt;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    @GetMapping("/orders-list")
    public String ordersList(Model model){
        model.addAttribute("orders",orderService.getAllOrders());
        return "admin/orders-list";
    }
    @GetMapping("/order-details/{orderId}")
    public String productDetails( @PathVariable("orderId") Long orderId,
                                  Model model){
        OrderDto orderDto=orderService.getOrderDetails(orderId);
        List<OrderStatusEnum> statuses=orderService.getStatusesToUpdate(orderId);
        List<SelectedProducts> products=orderService.setProductsToOrderDetailsPage(orderRepository.findByOrderId(orderId).getProducts());
        model.addAttribute(statuses);
        model.addAttribute("orderId",orderId);
        model.addAttribute("order",orderDto);
        model.addAttribute("products",products);
        return "admin/order-details";
    }
    @GetMapping("/updateOrderStatus")
    @ResponseBody
    public ResponseEntity<OrderDto> updateOrderStatus(@RequestParam("orderId")Long orderId,
                                            @RequestParam("newStatus")String newStatus){
        orderService.updateOrderStatus(orderId,newStatus);
        String status = String.valueOf(orderService.getStatus(orderId));
        OrderDto orderDto=new OrderDto();
        orderDto.setOrderStatus(status);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
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
    @PostMapping("/acceptReturn")
    public String acceptReturnRequest(@RequestParam("orderId")Long orderId){
        orderService.confirmReturn(orderId);
        return "redirect:/admin/order-details/"+orderId;
    }




}
