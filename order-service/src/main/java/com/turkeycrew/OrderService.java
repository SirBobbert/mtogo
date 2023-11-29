package com.turkeycrew;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void processOrder(Order orderRequest) {

        // TODO: Send a kafka message to topic "test123" with orderRequest

        try {
            String orderRequestJson = objectMapper.writeValueAsString(orderRequest);
//            kafkaTemplate.send("test123", orderRequestJson);
//            kafkaTemplate.send("test12", orderRequest.getUserId());

            kafkaTemplate.send("createOrderUserId", orderRequest.getUserId());

            orderRequest.getDeliveryId();

            Order order = new Order();
            order.setUserId(orderRequest.getUserId());
            order.setItems(orderRequest.getItems());
            //order.setTotalAmount(orderRequest.getTotalAmount());
            order.setStatus(OrderStatus.PENDING); // Set the initial status

            // Set the bidirectional relationship
            double totalAmount = 0.0;
            for (OrderItem orderItem : orderRequest.getItems()) {
                orderItem.setOrder(order);

                double itemTotalPrice = orderItem.getPrice() * orderItem.getQuantity();
                totalAmount += itemTotalPrice;

                orderItem.setTotalPrice(itemTotalPrice);
            }

            // Assign the items to the order
            order.setItems(orderRequest.getItems());
            // Save the order to generate order_id and cascade to order items
            order.setTotalAmount(totalAmount);
            orderRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Transactional(readOnly = true)
    public Order getOrderDetails(int orderId) throws Exception {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found with id: " + orderId));
    }


    @KafkaListener(topics = "updateOrderByDeliveryId", groupId = "order-group")
    public void listen(String message) {
        System.out.println("Received message from Kafka:");
        System.out.println(message);
        System.out.println("Received message from Kafka:");

        Order order = orderRepository.findLastOrder();
        order.setDeliveryId(Integer.parseInt(message));

        System.out.println(order.getDeliveryId());
        System.out.println(order.getDeliveryId());
        System.out.println(order.getDeliveryId());
        System.out.println(order.getDeliveryId());

        try {
            updateOrder(order.getOrderId(), order);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Order updateOrder(int orderId, Order updatedOrder) throws Exception {
        // Retrieve the existing order from the database
        Order existingOrder = orderRepository.findById(orderId).orElse(null);

        if (existingOrder != null) {
            // Update the fields based on the values in the updatedOrder
            existingOrder.setStatus(updatedOrder.getStatus());
            existingOrder.setItems(updatedOrder.getItems());
            existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
            existingOrder.setDeliveryId(updatedOrder.getDeliveryId());

            // Save the updated order back to the database
            return orderRepository.save(existingOrder);
        } else {
            throw new Exception("Order not found with id: " + orderId);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersForUser(int userId) {
        // Query orders for the specified user ID
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }
}
