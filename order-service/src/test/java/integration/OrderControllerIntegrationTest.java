package integration;

import com.turkeycrew.Order;
import com.turkeycrew.OrderServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrderServiceApplication.class)
public class OrderControllerIntegrationTest {

    private int port = 8081;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test_placeOrder() {
        String url = "http://localhost:" + port + "/api/orders/create/{restaurantId}";
        ResponseEntity<String> response = restTemplate.postForEntity(url, new Order(), String.class, 1);

        // Assert statements
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertEquals("Order processed successfully", response.getBody());
    }

    @Test
    public void test_getOrderDetails() {
        String url = "http://localhost:" + port + "/api/orders/{orderId}";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, 1);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Order Details"));
    }

    @Test
    public void test_updateOrderStatus() {
        String url = "http://localhost:" + port + "/api/orders/updateOrder/{orderId}";
        Map<String, String> status = new HashMap<>();
        status.put("status", "DELIVERED");

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.PUT, new HttpEntity<>(status), String.class, 1);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Order already has this status", response.getBody());
    }

    @Test
    public void test_getOrdersForUser() {
        String url = "http://localhost:" + port + "/api/orders/user/{userId}";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, 1);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Order"));
        // Modify the condition based on the actual response content
    }
}
