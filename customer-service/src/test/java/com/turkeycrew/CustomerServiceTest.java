package com.turkeycrew;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

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
        Customer customer = new Customer(1, "test@tasdsadsasaddest.test", "1234", "John Tester");

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
    void test_loginCustomer() {
        // Create a test customer
        Customer customer = new Customer(1, "test@test.test", "1234", "John Tester");

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);

        // Perform the login operation
        ResponseEntity<?> response = customerService.loginCustomertest(customer);
        System.out.println("Response Body: " + response.getBody());

        System.out.println(customer.getPassword());

        // Add your assertions here
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void test_successfulLogin() {
        // Create a test customer
        Customer customer = new Customer(1, "test@test.test", "1234", "John Tester");

        // Mock the behavior of the customerRepository
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);

        // Perform the login operation
        ResponseEntity<?> response = customerService.loginCustomertest(customer);
        System.out.println("Response Body: " + response.getBody());

        // Assert that the response status code is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



    @Test
    void logoutCustomer() {
    }
}