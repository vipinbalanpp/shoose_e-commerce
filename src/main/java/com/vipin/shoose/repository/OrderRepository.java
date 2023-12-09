package com.vipin.shoose.repository;

import com.vipin.shoose.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderInfo,Long> {
    OrderInfo findByOrderId(Long orderId);

    @Query("SELECT o FROM OrderInfo o " +
            "WHERE o.currentStatus = com.vipin.shoose.model.OrderStatusEnum.DELIVERED " +
            "AND KEY(o.status) = 'DELIVERED' " +
            "AND VALUE(o.status) BETWEEN :weekStart AND :weekEnd")
    List<OrderInfo> getWeeklyFromStartToEnd(
            @Param("weekStart") LocalDate weekStart,
            @Param("weekEnd") LocalDate weekEnd
    );
    @Query("SELECT o FROM OrderInfo o " +
            "WHERE o.currentStatus = com.vipin.shoose.model.OrderStatusEnum.DELIVERED " +
            "AND 'DELIVERED' = KEY(o.status) " +
            "AND :currentDate = VALUE(o.status)")
    List<OrderInfo> getDailyFromCurrentDay(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT o FROM OrderInfo o " +
            "WHERE o.currentStatus = com.vipin.shoose.model.OrderStatusEnum.DELIVERED " +
            "AND KEY(o.status) = 'DELIVERED' " +
            "AND VALUE(o.status) BETWEEN :monthStart AND :monthEnd")
    List<OrderInfo> getMonthlyFromStartToEnd(@Param("monthStart") LocalDate monthStart,@Param("monthEnd") LocalDate monthEnd);
    @Query("SELECT o FROM OrderInfo o " +
            "WHERE o.currentStatus = com.vipin.shoose.model.OrderStatusEnum.DELIVERED " +
            "AND KEY(o.status) = 'DELIVERED' " +
            "AND VALUE(o.status) BETWEEN :yearStart AND :yearEnd")
    List<OrderInfo> getYearlyFromStartToEnd(@Param("yearStart") LocalDate yearStart,@Param("yearEnd") LocalDate yearEnd);
    @Query("SELECT o FROM OrderInfo o " +
            "WHERE o.currentStatus = com.vipin.shoose.model.OrderStatusEnum.DELIVERED " +
            "AND KEY(o.status) = 'DELIVERED' " +
            "AND VALUE(o.status) BETWEEN :startDate AND :endDate")
    List<OrderInfo> monthlySalesReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
