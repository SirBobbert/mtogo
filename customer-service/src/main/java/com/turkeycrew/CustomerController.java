package com.turkeycrew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        ResponseEntity<String> response = customerService.createCustomer(customer);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return ResponseEntity.ok("Customer created successfully");
        } else {
            return response;
        }
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
        return customerService.loginCustomer(customer);
    }


}