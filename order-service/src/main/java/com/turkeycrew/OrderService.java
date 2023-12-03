package com.turkeycrew;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public ResponseEntity<String> processOrder(Integer restaurantId, Order orderRequest) {
        kafkaTemplate.send("createOrderUserId", orderRequest.getUserId());

        orderRequest.setRestaurantId(restaurantId);
        orderRequest.setTotalAmount(orderRequest.getTotalAmount());

        double totalAmount = 0.0;
        for (OrderItem orderItem : orderRequest.getItems()) {
            double itemTotalPrice = orderItem.getPrice() * orderItem.getQuantity();
            totalAmount += itemTotalPrice;
        }

        orderRequest.setStatus(OrderStatus.PENDING);
        orderRequest.setTotalAmount(totalAmount);

        orderRepository.save(orderRequest);

        // Return a ResponseEntity with a status of 200 OK and a success message
        return ResponseEntity.status(HttpStatus.CREATED).body("Order processed successfully");
    }

    public ResponseEntity<String> getOrderDetails(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            Order orderDetails = order.get();

            String response = "Order Details: " + orderDetails.toString();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
    }

    @KafkaListener(topics = "updateOrderByDeliveryId", groupId = "order-group")
    public void listen(String message) {
        Order order = orderRepository.findLastOrder();
        order.setDeliveryId(Integer.parseInt(message));
        try {
            updateOrder(order.getOrderId(), objectMapper.readValue("{\"status\":\"PROCESSING\"}", Map.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> updateOrder(Integer orderId, Map<String, String> requestBody) {
        String statusValue = requestBody.get("status");

        if (statusValue == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status is missing in the request");
        }

        OrderStatus status = OrderStatus.fromValue(statusValue);

        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            order.setStatus(status);
            orderRepository.save(order);

            return ResponseEntity.ok("Order updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
    }


    public ResponseEntity<String> getOrdersForUser(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for this user");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(orders.toString());
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @KafkaListener(topics = "GetAllOrdersTrigger", groupId = "order-group")
    public void getAllDeliveriesTrigger(String message) {
        System.out.println("Received message from Kafka:");
        System.out.println(message);
        kafkaTemplate.send("GetAllOrders", getAllOrders());
    }


}
