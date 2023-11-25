package com.turkeycrew;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/find/{customerId}")
    public ResponseEntity<?> getCustomer(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer customerId, @RequestBody Customer customer) {
        return customerService.updateCustomer(customerId, customer);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer customerId) {
        return customerService.deleteCustomer(customerId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Customer customer) {
        return customerService.loginCustomer(customer.getEmail(), customer.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutCustomer(HttpServletResponse response) {
        customerService.logoutCustomer(response);
        return ResponseEntity.ok("Logout successful");
    }
}