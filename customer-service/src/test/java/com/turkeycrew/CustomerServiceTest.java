package com.turkeycrew;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;


    @Test
    public void test_createCustomer() {
        // Arrange
        Customer customer = new Customer(1, "test@test.dk", "1234", "John Tester");

        // Mock behaviour
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        ResponseEntity<String> response = customerService.createCustomer(customer);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void test_getCustomerById() {
        // Arrange
        Customer customer = new Customer(1, "test@test.test", "1234", "Test Tester");
        when(customerRepository.existsById(customer.getId())).thenReturn(true);
        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));

        // Act
        ResponseEntity<String> response = customerService.getCustomerById(customer.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
        assertNotNull(response.getBody()); // Check if the response body is not null
    }

    @Test
    void test_UpdateCustomer() {
        // Arrange
        Customer customer = new Customer(1, "Test@test.dk", "1234", "John Tester");

        // Mock the behavior of customerRepository.findById to return Optional containing the customer
        when(customerRepository.existsById(customer.getId())).thenReturn(true);

        // Act
        ResponseEntity<?> response = customerService.updateCustomer(customer.getId(), customer);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
        assertNotNull(response.getBody()); // Check if the response body is not null
    }


    @Test
    void test_deleteCustomer() {
        // Arrange
        Customer customer = new Customer(1, "test@test.test", "1234", "John Tester");

        // Mock behavior
        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
        when(customerRepository.existsById(customer.getId())).thenReturn(true);

        // Act
        ResponseEntity<?> response = customerService.deleteCustomer(customer.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
        assertEquals("Customer deleted successfully", response.getBody()); // Check the response body

        verify(customerRepository, times(1)).findById(customer.getId()); // Verify that findById is called once
        verify(customerRepository, times(1)).deleteById(customer.getId()); // Verify that deleteById is called once
    }


    @Test
    void test_loginCustomer() {
        // Arrange
        String email = "test@test.test";
        String password = "password";
        Customer customer = new Customer(1, email, password, "John Tester");

        when(customerRepository.findByEmailAndPassword(email, password)).thenReturn(customer);

        // Act
        ResponseEntity<?> response = customerService.loginCustomer(email, password);

        // Assert
        assertEquals(ResponseEntity.ok("Login successful!"), response);
        // You may add additional assertions or verifications if needed
    }

    @Test
    void logoutCustomer() {
        // Arrange
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Act
        ResponseEntity<?> logoutResponse = customerService.logoutCustomer(response);

        // Assert
        assertEquals(ResponseEntity.ok("Logout successful"), logoutResponse);
        // You may add additional assertions or verifications if needed
    }
}