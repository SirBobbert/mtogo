package com.turkeycrew;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPlaceOrderIntegration() throws URISyntaxException {
        // Arrange
        final String baseUrl = "http://localhost:8081/api/orders/create/1"; // Update the port if needed
        URI uri = new URI(baseUrl);

        Order order = new Order();
        order.setUserId(1);
        // Set other fields as needed

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Order> request = new HttpEntity<>(order, headers);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        // Add more assertions based on your application logic
    }

    // Add more integration tests for other endpoints as needed
}
