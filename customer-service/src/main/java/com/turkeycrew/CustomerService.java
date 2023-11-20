package com.turkeycrew;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // TODO: Implement the following methods:
    // - createCustomer
    // - getCustomerById
    // - updateCustomer
    // - deleteCustomer
    // - also make sure that enough tests and error handling are written for each of these methods

    public ResponseEntity<String> createCustomer(Customer customer) {

        if (!isValidEmail(customer.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
        }

        if (customerRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the email " + customer.getEmail() + " already exists");
        }

        String encryptedPassword = encodePassword(customer.getPassword());
        customer.setPassword(encryptedPassword);

        customer = customerRepository.save(customer);
        System.out.println("Customer: " + customer + " created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(customer.toString());
    }

    public ResponseEntity<String> getCustomerById(Integer customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            String response = "Customer ID: " + customer.getId() +
                    "\nUsername: " + customer.getFullName() +
                    "\nEmail: " + customer.getEmail();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }
    }


    // Email validation helper function
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Password encoder helper functions
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public ResponseEntity<?> updateCustomer(Integer customerId, Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Customer customerToUpdate = customerOptional.get();

            if (customer.getEmail() != null) {
                String newEmail = customer.getEmail();

                if (newEmail.equals(customerToUpdate.getEmail())) {
                    return ResponseEntity.status(HttpStatus.OK).body("This email address is already yours");
                }

                if (!isValidEmail(newEmail)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
                }

                if (customerRepository.existsByEmail(newEmail)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the email " + newEmail + " already exists");
                }

                customerToUpdate.setEmail(newEmail);
            }

            if (customer.getEmail() != null) {
                customerToUpdate.setEmail(customer.getEmail());
            }

            if (customer.getPassword() != null) {
                String encryptedPassword = encodePassword(customer.getPassword());
                customerToUpdate.setPassword(encryptedPassword);
            }

            customerRepository.save(customerToUpdate);
            return ResponseEntity.ok("Customer updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }
    }
}
