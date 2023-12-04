//package integration;
//
//import com.turkeycrew.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrderServiceApplication.class)
//public class OrderControllerIntegrationTest {
//
//    private int port = 8081;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @MockBean
//    private OrderService orderService;
//
//    @Test
//    @Rollback
//    public void test_placeOrder() {
//        String url = "http://localhost:" + port + "/api/orders/create/{restaurantId}";
//
//        Order orderRequest = new Order();
//        orderRequest.setUserId(1);  // Set the user ID
//        // Add items to the order (you may need to adjust this based on your Order class structure)
//        List<OrderItem> items = new ArrayList<>();
//
//        OrderItem item1 = new OrderItem().builder()
//                .itemName("Item1")
//                .quantity(2)
//                .price(5.0)
//                .totalPrice(2 * 5) // You may want to calculate the total price based on quantity and price
//                .build();
//
//        OrderItem item2 = OrderItem.builder()
//                .itemName("Item2")
//                .quantity(1)
//                .price(3.0)
//                .totalPrice(1 * 3.0)
//                .build();
//
//        items.add(item1);
//        items.add(item2);
//
//        orderRequest.setItems(items);
//        orderRequest.setTotalAmount(items.get(0).getTotalPrice() + items.get(1).getTotalPrice());
//        orderRequest.setStatus(OrderStatus.PENDING);
//
//
//        // Mock the behavior of the orderService.processOrder method
//        when(orderService.processOrder(1, orderRequest)).thenReturn(ResponseEntity.ok("Order processed successfully"));
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, orderRequest, String.class, 1);
//
//        // Assert statements
//        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
//        assertEquals("Order processed successfully", response.getBody());
//    }
//
//    @Test
//    @Rollback
//    public void test_getOrderDetails() {
//        String url = "http://localhost:" + port + "/api/orders/{orderId}";
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, 1);
//
//        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
//        assertTrue(response.getBody().contains("Order Details"));
//    }
//
//    @Test
//    @Rollback
//    public void test_updateOrderStatus() {
//        String initialUrl = "http://localhost:" + port + "/api/orders/updateOrder/1"; // Replace 1 with the actual orderId
//
//        HttpHeaders initialHeaders = new HttpHeaders();
//        initialHeaders.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> initialRequestBody = new HashMap<>();
//        initialRequestBody.put("status", "processing"); // Set a different status
//
//        HttpEntity<Map<String, String>> initialRequestEntity = new HttpEntity<>(initialRequestBody, initialHeaders);
//
//        ResponseEntity<String> initialResponse = restTemplate.exchange(initialUrl, HttpMethod.PUT, initialRequestEntity, String.class);
//
//        System.out.println("Request: " + initialRequestEntity); // Logging request details
//        System.out.println("Response: " + initialResponse); // Logging response details
//
//        assertEquals(HttpStatus.OK.value(), initialResponse.getStatusCodeValue());
//        assertEquals("Order updated successfully", initialResponse.getBody());
//    }
//
//    @Test
//    @Rollback
//    public void test_getOrdersForUser() {
//        String url = "http://localhost:" + port + "/api/orders/user/{userId}";
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, 1);
//
//        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
//        assertTrue(response.getBody().contains("Order"));
//        // Modify the condition based on the actual response content
//    }
//}
