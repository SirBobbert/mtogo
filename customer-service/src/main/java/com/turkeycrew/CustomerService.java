package com.turkeycrew;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

            String response = "Customer ID: " + customer.getId() + "\nUsername: " + customer.getFullName() + "\nEmail: " + customer.getEmail();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }
    }


    // Email validation helper function
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
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

    // Helper functions
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateToken(Integer customerId) {
        // In a real-world scenario, you'd want to customize the claims (e.g., add user roles, expiration time, etc.)
        return Jwts.builder().setSubject(customerId.toString()).signWith(SignatureAlgorithm.HS256, "secretKey") // Use a secure secret key
                .compact();
    }

    void clearTokenFromClient(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
