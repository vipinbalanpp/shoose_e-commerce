package com.vipin.shoose.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long orderId;
    private List<SelectedProducts>products;
    private LocalDate orderedDate;
    private String customerName;
    private Float amount;
    private String paymentMethode;
    private String orderStatus;
    private List<String>statuses;
}
