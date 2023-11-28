package com.turkeycrew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void processOrder(Order orderRequest) {


        String customerAddress = "this is a test";
        kafkaTemplate.send("test123", customerAddress);


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

    }

    @Transactional(readOnly = true)
    public Order getOrderDetails(int orderId) throws Exception {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found with id: " + orderId));
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
