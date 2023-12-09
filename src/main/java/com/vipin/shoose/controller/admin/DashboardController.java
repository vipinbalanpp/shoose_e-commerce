package com.vipin.shoose.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vipin.shoose.service.OrderService;
import com.vipin.shoose.service.ProductService;
import com.vipin.shoose.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @GetMapping("/admin/home")
    public String adminHome(Model model) throws JsonProcessingException {
        int totalCustomers=userService.getTotalUsers();
        int soldProducts=orderService.getSoldProducts();
        double totalSales=orderService.getTotalSales();
        Map<String,Double>weeklySales=orderService.getWeeklySales();
        Map<String,Long>weeklyCount=orderService.getWeeklyCount();
        Map<String,Double>dailySales=orderService.getDailySales();
        Map<String,Long>dailyCount=orderService.getDailyCount();
        Map<String,Double>monthlySales=orderService.getMonthlySales();
        Map<String,Long>monthlySalesCount=orderService.getMonthlySalesCount();
        Map<String,Double>yearlySales=orderService.getYearlySales();
        Map<String,Long>yearlySalesCount=orderService.getYearlySalesCount();
        ObjectMapper objectMapper=new ObjectMapper();
        model.addAttribute("yearlySales",objectMapper.writeValueAsString(yearlySalesCount));
        model.addAttribute("yearlySalesCount",objectMapper.writeValueAsString(yearlySalesCount));
        model.addAttribute("monthlySales",objectMapper.writeValueAsString(monthlySales));
        model.addAttribute("monthlySalesCount",objectMapper.writeValueAsString(monthlySalesCount));
        model.addAttribute("dailySales",objectMapper.writeValueAsString(dailySales));
        model.addAttribute("dailyCount",objectMapper.writeValueAsString(dailyCount));
        model.addAttribute("weeklyCount",objectMapper.writeValueAsString(weeklyCount));
        model.addAttribute("weeklySales", objectMapper.writeValueAsString(weeklySales));
        model.addAttribute("totalCustomers",totalCustomers);
        model.addAttribute("soldProducts",soldProducts);
        model.addAttribute("totalSales",totalSales);
        return "admin/home";
    }
    @GetMapping("/date-wise-download")
    public ResponseEntity<byte[]> downloadDateWise(@RequestParam("startDate")String startDate,
                                                   @RequestParam("endDate")String endDate){
        try {

            byte[] invoiceBytes = orderService.salesReportDayWise(startDate,endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "sales-report.pdf");

            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/monthly-sales-report")
    public ResponseEntity<byte[]> monthlySalesReport(){
        try {

            byte[] invoiceBytes = orderService.monthlySalesReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "monthly-sales-report.pdf");

            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/yearly-sales-report")
    public ResponseEntity<byte[]> yearlySalesReport(){
        try {

            byte[] invoiceBytes = orderService.yearlySalesReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "yearly-sales-report.pdf");

            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
