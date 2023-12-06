package com.turkeycrew;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.turkeycrew.CustomerUtils.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CustomerService(CustomerRepository customerRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "createOrderUserId", groupId = "customer-group")
    public void listen(String message) {

        Optional<Customer> customer = customerRepository.findById(Integer.valueOf(message));

        if (customer.isPresent()) {
            kafkaTemplate.send("createDeliveryByUserId", customer.get().getAddress());
        }

        System.out.println("CUSTOMER SERVICE U ARE HERE");
        System.out.println("CUSTOMER SERVICE U ARE HERE");
        System.out.println("CUSTOMER SERVICE U ARE HERE");
        System.out.println("CUSTOMER SERVICE U ARE HERE");
        System.out.println("CUSTOMER SERVICE U ARE HERE");
        System.out.println("CUSTOMER SERVICE U ARE HERE");
        System.out.println("CUSTOMER SERVICE U ARE HERE");
    }

    public ResponseEntity<String> createCustomer(Customer customer) {

        if (!isValidEmail(customer.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
        }

        if (customerRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the email " + customer.getEmail() + " already exists");
        }

        customer.setPassword(encodePassword(customer.getPassword()));

        customer = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer.toString());
    }

    public ResponseEntity<String> getCustomerById(Integer customerId) {

        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.badRequest().body("Customer not found with ID: " + customerId);
        }

        return new ResponseEntity<>(customerRepository.findById(customerId).toString(), HttpStatus.OK);
    }

    public ResponseEntity<?> updateCustomer(Integer customerId, Customer customer) {

        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }

        if (customerRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the email " + customer.getEmail() + " already exists");
        }

        if (customerRepository.existsByAddress(customer.getAddress())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the address " + customer.getAddress() + " already exists");
        }

        customer.setPassword(encodePassword(customer.getPassword()));

        customer.setId(customerId);
        customerRepository.save(customer);

        return ResponseEntity.ok("Customer updated successfully");
    }

    public ResponseEntity<?> deleteCustomer(Integer customerId) {
        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }

        Optional<Customer> customerToDelete = customerRepository.findById(customerId);

        if (customerToDelete != null) {
            customerRepository.deleteById(customerId);
            return ResponseEntity.ok("Customer deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }
    }



    public ResponseEntity<?> loginCustomer(String email, String password) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);

        if (customerOptional.isPresent() && CustomerUtils.matches(password, customerOptional.get().getPassword())) {
            Customer customer = customerOptional.get();
            String authToken = CustomerUtils.generateToken(customer.getId());

            // You may want to store the token in a cookie or send it in the response as needed
            // For example, setting it in a cookie:
            // response.addCookie(new Cookie("authToken", authToken));

            // Create a map to hold the response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "Login successful");
            responseData.put("customerId", customer.getId());

            // Return a response with the map and HTTP status OK
            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }



    public ResponseEntity<?> logoutCustomer(HttpServletResponse response) {
        clearTokenFromClient(response);
        return ResponseEntity.ok("Logout successful");
    }
}
