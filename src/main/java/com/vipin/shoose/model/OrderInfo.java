package com.vipin.shoose.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Data
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private PaymentMethod paymentMethode;
    private LocalDate orderedDate;
    private Float amountPaid;
    private Float totalAmount;
    private Float discountAmount;
    @ManyToOne
    private User user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddress shippingAddress;
    @ElementCollection
    @CollectionTable(name = "order_status")
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "status")
    @Column(name = "timestamp")
    private Map<OrderStatusEnum, LocalDate>status;
    private OrderStatusEnum currentStatus;
    @ElementCollection
    @CollectionTable(name = "order_products",
            joinColumns = @JoinColumn(name = "order_info_order_id"))
    @MapKeyJoinColumn(name = "variant_id")
    @Column(name = "quantity")
    private Map<Variant,Long>products;
    public OrderInfo(){
        this.status=new LinkedHashMap<>();
        this.products=new HashMap<>();
    }
    public void setProducts(Variant variant,Long quantity) {
        products.put(variant,quantity);
    }
    public void setStatus(OrderStatusEnum orderStatusEnum,LocalDate localDate){
        this.status.put(orderStatusEnum,localDate);
    }
    public Map<OrderStatusEnum, LocalDate> getStatusesInOrder() {
        // Use a LinkedHashMap to maintain the insertion order
        return new LinkedHashMap<>(status);
    }
}
