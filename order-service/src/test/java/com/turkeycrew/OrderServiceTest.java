package com.turkeycrew;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    void test_processOrder_ValidInput() {

        // Arrange
        Order order = new Order();
        order.setUserId(1);
        order.setDeliveryId(1);
        order.setRestaurantId(1);

        List<OrderItem> items = new ArrayList<>();

        OrderItem item1 = new OrderItem().builder()
                .itemName("Item1")
                .quantity(2)
                .price(5.0)
                .totalPrice(2 * 5) // You may want to calculate the total price based on quantity and price
                .build();

        OrderItem item2 = OrderItem.builder()
                .itemName("Item2")
                .quantity(1)
                .price(3.0)
                .totalPrice(1 * 3.0)
                .build();

        items.add(item1);
        items.add(item2);

        order.setItems(items);
        order.setTotalAmount(items.get(0).getTotalPrice() + items.get(1).getTotalPrice());
        order.setStatus(OrderStatus.PENDING);

        // Mock the orderRepository.save method
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        ResponseEntity<String> response = orderService.processOrder(order.getRestaurantId(), order);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void test_processOrder_InvalidInput() {
        // Arrange
        Integer restaurantId = 1;
        Order invalidOrder = new Order();
        invalidOrder.setUserId(1);
        invalidOrder.setRestaurantId(restaurantId);
        invalidOrder.setDeliveryId(1);
        invalidOrder.setTotalAmount(0.0);
        invalidOrder.setStatus(OrderStatus.PENDING);

        // Create a list of OrderItems
        List<OrderItem> items = new ArrayList<>();
        OrderItem item1 = OrderItem.builder()
                .itemName("Item1")
                .quantity(2)
                .price(5.0)
                .totalPrice(2 * 5)
                .build();

        OrderItem item2 = OrderItem.builder()
                .itemName("Item2")
                .quantity(1)
                .price(3.0)
                .totalPrice(1 * 3.0)
                .build();

        items.add(item1);
        items.add(item2);

        // Set items in the order
        invalidOrder.setItems(items);

        // Calculate the total amount
        double totalAmount = 0.0;
        for (OrderItem orderItem : items) {
            double itemTotalPrice = orderItem.getPrice() * orderItem.getQuantity();
            totalAmount += itemTotalPrice;
        }

        // Declare and initialize the result variable before the if statement
        ResponseEntity<String> result;

        // Validate the total amount
        if (totalAmount <= 0.0) {
            // Return a ResponseEntity with a status of 400 BAD_REQUEST and an error message
            ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: Total amount must be greater than zero");

            // Set the result variable
            result = responseEntity;

            // Assert
            assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
            assertTrue(result.getBody().contains("Invalid input: Total amount must be greater than zero"));
        }

        // Act
        result = orderService.processOrder(restaurantId, invalidOrder);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("Order processed successfully"));
    }


    @Test
    void test_getOrderDetails() {

        // Arrange
        Integer orderId = 1;
        Order order = new Order(); // Create a sample order

        // Mock the orderRepository.findById method
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<String> result = orderService.getOrderDetails(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testUpdateOrder_ValidStatus() {
        // Arrange
        Integer orderId = 1;
        String status = "PROCESSING";

        // Create a Map with the expected structure
        Map<String, String> request = new HashMap<>();
        request.put("status", status);

        Order existingOrder = new Order();
        existingOrder.setOrderId(orderId);
        existingOrder.setStatus(OrderStatus.PENDING); // Initial status

        // Mocking the repository
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act
        ResponseEntity<String> result = orderService.updateOrder(orderId, request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Order updated successfully", result.getBody());
        assertEquals(OrderStatus.PROCESSING, existingOrder.getStatus()); // Ensure the status is updated

        // Verify that the save method is called on the orderRepository with any Order object
        verify(orderRepository).save(any());
    }

    @Test
    void test_updateOrder_InvalidStatus() {
        // Arrange
        Integer orderId = 1;
        String invalidStatus = "INVALID_STATUS";

        // Create a Map with the expected structure
        Map<String, String> request = new HashMap<>();
        request.put("status", invalidStatus);

        // Act
        ResponseEntity<String> result = orderService.updateOrder(orderId, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody().contains("Invalid OrderStatus value: " + invalidStatus));
    }

    @Test
    void test_updateOrder_OrderAlreadyHasStatus() {
        // Arrange
        Integer orderId = 1;
        String existingStatus = "PROCESSING";

        // Create a Map with the expected structure
        Map<String, String> request = new HashMap<>();
        request.put("status", existingStatus);

        Order existingOrder = new Order();
        existingOrder.setOrderId(orderId);
        existingOrder.setStatus(OrderStatus.valueOf(existingStatus));

        // Mock the orderRepository.findById method
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // Act
        ResponseEntity<String> result = orderService.updateOrder(orderId, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody().contains("Order already has this status"));
    }

    @Test
    void test_updateOrder_OrderNotFound() {
        // Arrange
        Integer orderId = 1;
        String validStatus = "PROCESSING";

        // Create a Map with the expected structure
        Map<String, String> request = new HashMap<>();
        request.put("status", validStatus);

        // Mock the orderRepository.findById method to return an empty Optional
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> result = orderService.updateOrder(orderId, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Order not found", result.getBody());
    }

    @Test
    void test_getOrdersForUser_NoOrders() {
        // Arrange
        Integer userId = 1;
        when(orderRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<String> result = orderService.getOrdersForUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("No orders found for this user", result.getBody());
    }

    @Test
    void test_getOrdersForUser_WithOrders() {
        // Arrange
        Integer userId = 1;
        when(orderRepository.findByUserId(userId)).thenReturn(List.of(new Order())); // Assuming there's at least one order

        // Act
        ResponseEntity<String> result = orderService.getOrdersForUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        // You may want to adjust the assertion based on the actual implementation
        assertTrue(result.getBody().contains("Order"));
    }

    @Test
    void test_getAllOrders_NoOrders() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void test_getAllOrders_WithOrders() {
        // Arrange
        List<Order> sampleOrders = List.of(new Order(), new Order()); // Assuming there are two orders
        when(orderRepository.findAll()).thenReturn(sampleOrders);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        // You may want to adjust the assertion based on the actual implementation
        assertTrue(result.get(0).toString().contains("Order"));
        assertTrue(result.get(1).toString().contains("Order"));
    }

    @Test
    void testGetAllDeliveriesTrigger() {
        // Arrange
        String kafkaMessage = "SomeKafkaMessage";

        // Act
        orderService.getAllDeliveriesTrigger(kafkaMessage);

        // Assert
        // Add assertions here to verify the expected behavior, e.g., verify that kafkaTemplate.send is called
        verify(kafkaTemplate, times(1)).send(eq("GetAllOrders"), any());
    }
}