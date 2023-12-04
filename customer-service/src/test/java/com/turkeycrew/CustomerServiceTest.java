//package com.turkeycrew;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class CustomerServiceTest {
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @InjectMocks
//    private CustomerService customerService;
//
//
//    @Test
//    public void test_createCustomer() {
//        // Arrange
//        Customer customer = new Customer(1, "test@test.dk", "1234", "John Tester");
//
//        // Mock behaviour
//        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
//        when(customerRepository.save(customer)).thenReturn(customer);
//
//        // Act
//        ResponseEntity<String> response = customerService.createCustomer(customer);
//
//        // Assert
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//    }
//
//    @Test
//    void test_getCustomerById() {
//        // Arrange
//        Customer customer = new Customer(1, "test@test.test", "1234", "Test Tester");
//        when(customerRepository.existsById(customer.getId())).thenReturn(true);
//        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
//
//        // Act
//        ResponseEntity<String> response = customerService.getCustomerById(customer.getId());
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
//        assertNotNull(response.getBody()); // Check if the response body is not null
//    }
//
//    @Test
//    void test_UpdateCustomer() {
//        // Arrange
//        Customer customer = new Customer(1, "Test@test.dk", "1234", "John Tester");
//
//        // Mock the behavior of customerRepository.findById to return Optional containing the customer
//        when(customerRepository.existsById(customer.getId())).thenReturn(true);
//
//        // Act
//        ResponseEntity<?> response = customerService.updateCustomer(customer.getId(), customer);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
//        assertNotNull(response.getBody()); // Check if the response body is not null
//    }
//
//
//    @Test
//    void test_deleteCustomer() {
//        // Arrange
//        Customer customer = new Customer(1, "test@test.test", "1234", "John Tester");
//
//        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
//
//        // Act
//        ResponseEntity<?> response = customerService.deleteCustomer(customer.getId());
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check if the status code is OK
//        assertEquals("Customer deleted successfully", response.getBody()); // Check the response body
//    }
//
//
//    @Test
//    void test_loginCustomer() {
//    }
//
//    @Test
//    void logoutCustomer() {
//    }
//}