package com.turkeycrew;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.turkeycrew.CustomerUtils.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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

        customer.setPassword(encodePassword(customer.getPassword()));

        customer.setId(customerId);
        customerRepository.save(customer);

        return ResponseEntity.ok("Customer updated successfully");
    }

    public ResponseEntity<?> deleteCustomer(Integer customerId) {
        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }

        customerRepository.deleteById(customerId);
        return ResponseEntity.ok("Customer deleted successfully");
    }


    public ResponseEntity<?> loginCustomer(String email, String password) {

        Customer customer = customerRepository.findByEmailAndPassword(email, password);

        if (customer != null) {
            generateToken(customer.getId());
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    public ResponseEntity<?> logoutCustomer(HttpServletResponse response) {
        clearTokenFromClient(response);
        return ResponseEntity.ok("Logout successful");
    }
}
