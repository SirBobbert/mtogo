package integration;

import com.turkeycrew.Order;
import com.turkeycrew.OrderService;
import com.turkeycrew.OrderServiceApplication;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrderServiceApplication.class)
public class OrderControllerIntegrationTest {

    private int port = 8081;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OrderService orderService;

    @Test
    @Rollback
    public void test_placeOrder() {
        String url = "http://localhost:" + port + "/api/orders/create/{restaurantId}";

        // Mock the behavior of the orderService.processOrder method
        when(orderService.processOrder(1, new Order())).thenReturn(ResponseEntity.ok("Order processed successfully"));

        ResponseEntity<String> response = restTemplate.postForEntity(url, new Order(), String.class, 1);

        // Assert statements
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals("Order processed successfully", response.getBody());
    }


    @Test
    @Rollback
    public void test_getOrderDetails() {
        String url = "http://localhost:" + port + "/api/orders/{orderId}";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, 1);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Order Details"));
    }

    @Test
    @Rollback
    public void test_updateOrderStatus() {
        String initialUrl = "http://localhost:" + port + "/api/orders/updateOrder/1"; // Replace 1 with the actual orderId

        HttpHeaders initialHeaders = new HttpHeaders();
        initialHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> initialRequestBody = new HashMap<>();
        initialRequestBody.put("status", "processing"); // Set a different status

        HttpEntity<Map<String, String>> initialRequestEntity = new HttpEntity<>(initialRequestBody, initialHeaders);

        ResponseEntity<String> initialResponse = restTemplate.exchange(initialUrl, HttpMethod.PUT, initialRequestEntity, String.class);

        System.out.println("Request: " + initialRequestEntity); // Logging request details
        System.out.println("Response: " + initialResponse); // Logging response details

        assertEquals(HttpStatus.OK.value(), initialResponse.getStatusCodeValue());
        assertEquals("Order updated successfully", initialResponse.getBody());
    }

    @Test
    @Rollback
    public void test_getOrdersForUser() {
        String url = "http://localhost:" + port + "/api/orders/user/{userId}";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, 1);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Order"));
        // Modify the condition based on the actual response content
    }
}
