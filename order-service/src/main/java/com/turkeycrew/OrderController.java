package com.turkeycrew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    // User address
    @PostMapping("/create/{restaurantId}")
    public ResponseEntity<String> placeOrder(@PathVariable Integer restaurantId, @RequestBody Order orderRequest) {
        return orderService.processOrder(restaurantId, orderRequest);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<String> getOrderDetails(@PathVariable Integer orderId) {
        return orderService.getOrderDetails(orderId);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int orderId, @RequestBody Map<String, String> status) {
        return orderService.updateOrder(orderId, status);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<String> getOrdersForUser(@PathVariable int userId) {
        return orderService.getOrdersForUser(userId);
    }

}
