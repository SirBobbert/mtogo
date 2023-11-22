package com.turkeycrew;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.turkeycrew.CustomerUtils.*;
import static java.util.regex.Pattern.matches;

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

            String response = "Customer ID: " + customer.getId() + "\nUsername: " + customer.getFullName() + "\nEmail: " + customer.getEmail();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }
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

    public ResponseEntity<?> deleteCustomer(Integer customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            customerRepository.deleteById(customerId);
            return ResponseEntity.ok("Customer deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }
    }

    public ResponseEntity<?> loginCustomer(Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(customer.getEmail());

        if (customerOptional.isPresent()) {
            Customer customerToLogin = customerOptional.get();

            if (matches(customer.getPassword(), customerToLogin.getPassword())) {
                // Generate a JWT token
                String token = generateToken(customerToLogin.getId());

                // Return the token along with a success message
                return ResponseEntity.ok("Login successful. Token: " + token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with email: " + customer.getEmail());
        }
    }

    public void logoutCustomer(HttpServletResponse response) {
        clearTokenFromClient(response);
    }

    public ResponseEntity<?> loginCustomertest(Customer customer) {
        // Retrieve the customer from the database based on the email
        // This assumes that the email is unique
        Customer storedCustomer = customerRepository.findByEmail(customer.getEmail()).orElse(null);

        if (storedCustomer != null && isValidPassword(customer.getPassword(), storedCustomer.getPassword())) {
            // Authentication successful
            return ResponseEntity.ok("Login successful");
        } else {
            // Authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password");
        }
    }

    private boolean isValidPassword(String inputPassword, String storedPassword) {
        // You need to implement your password comparison logic here
        // This is a simple example assuming plain text passwords
        return inputPassword.equals(storedPassword);
    }
}
