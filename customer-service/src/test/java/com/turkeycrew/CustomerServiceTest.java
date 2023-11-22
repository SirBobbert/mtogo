package com.turkeycrew;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.turkeycrew.CustomerUtils.passwordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;


    @Test
    public void test_createCustomer() {
        // Arrange
        Customer customer = new Customer(1, "test@test.test", "1234", "John Tester");

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
        Customer customer = new Customer(1, "test@test.test", "1234", "John Tester");
        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));

        // Act
        ResponseEntity<String> response = customerService.getCustomerById(customer.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
        assertNotNull(response.getBody()); // Check if the response body is not null
        // Add more assertions if needed
    }

    @Test
    void test_UpdateCustomer() {
        // Arrange
        Integer customerId = 1;
        Customer existingCustomer = new Customer(customerId, "existing@test.com", "oldpassword", "Old User");
        Customer updatedCustomer = new Customer(customerId, "updated@test.com", "newpassword", "Updated User");

        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(existingCustomer));
        when(customerRepository.existsByEmail(updatedCustomer.getEmail())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(updatedCustomer);

        // Act
        ResponseEntity<?> response = customerService.updateCustomer(customerId, updatedCustomer);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
        assertEquals("Customer updated successfully", response.getBody()); // Check the response body
        assertNotEquals("oldpassword", existingCustomer.getPassword()); // Ensure password is updated
        assertTrue(passwordEncoder.matches("newpassword", existingCustomer.getPassword())); // Validate encoded password
    }


    @Test
    void test_deleteCustomer() {
        // Arrange
        Customer customer = new Customer(1, "test@test.test", "1234", "John Tester");

        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));

        // Act
        ResponseEntity<?> response = customerService.deleteCustomer(customer.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
        assertEquals("Customer deleted successfully", response.getBody()); // Check the response body
    }

    @Test
    void loginCustomer() {

    }

    @Test
    void logoutCustomer() {
    }
}