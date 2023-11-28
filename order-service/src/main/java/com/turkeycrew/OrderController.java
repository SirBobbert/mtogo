package com.turkeycrew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order orderRequest) {
        try {
            orderService.processOrder(orderRequest);
            return ResponseEntity.ok("Order placed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error placing order: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable int orderId) {
        try {
            Order order = orderService.getOrderDetails(orderId);
            return ResponseEntity.ok(order);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable int orderId, @RequestBody Order updatedOrder) {
        try {
            Order savedOrder = orderService.updateOrder(orderId, updatedOrder);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable int userId) {
        try {
            List<Order> userOrders = orderService.getOrdersForUser(userId);
            return ResponseEntity.ok(userOrders);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
