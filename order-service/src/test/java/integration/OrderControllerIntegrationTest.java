package integration;

import com.turkeycrew.Order;
import com.turkeycrew.OrderServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

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
    public void test_PlaceOrder() {
        // Implement your test logic using restTemplate
        // For example:
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

        // Assuming orderId is 1
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.PUT, new HttpEntity<>(status), String.class, 1);

        System.out.println("Request URL: " + url);
        System.out.println("Request Method: " + HttpMethod.PUT);
        System.out.println(response.getBody());
        System.out.println(response.getStatusCodeValue());
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getHeaders().getContentType());
        System.out.println(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON));

        // Update the assertions to expect a 400 status code and the specific error message
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Order already has this status", response.getBody());
    }







    // Add more test methods for other endpoints
}
