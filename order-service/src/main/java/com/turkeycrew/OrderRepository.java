package com.turkeycrew;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);

    @Query("SELECT o FROM Order o WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)")
    Order findLastOrder();
}
