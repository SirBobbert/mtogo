package com.turkeycrew;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    //---------------courier api's---------------

    //register new courier
    @PostMapping("/courier/register")
    public ResponseEntity<?> registerCourier(@RequestBody Courier courier) {
        ResponseEntity<String> response = deliveryService.createCourier(courier);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return ResponseEntity.ok("Courier created successfully");
        } else {
            return response;
        }
    }

    //find courier by id
    @GetMapping("/courier/find/{courierId}")
    public ResponseEntity<?> getCourierById(@PathVariable Integer courierId) {
        return deliveryService.getCourierById(courierId);
    }

    //update courier with email
    @PutMapping("/courier/update/{courierId}")
    public ResponseEntity<?> updateCourierEmail(@PathVariable Integer courierId, @RequestBody Map<String, String> emailMap) {
        String newEmail = emailMap.get("email");
        return deliveryService.updateCourierEmail(courierId, newEmail);
    }


    @DeleteMapping("/courier/delete/{courierId}")
    public ResponseEntity<?> deleteCourier(@PathVariable Integer courierId) {
        return deliveryService.deleteCourier(courierId);
    }

    //delete courier by id

    //---------------deliveryinfo api's---------------
    @PostMapping("/create")
    @KafkaListener(topics = "test123")
    public ResponseEntity<?> createDelivery(@RequestBody DeliveryInfo deliveryInfo, String customerAddress) {
        ResponseEntity<String> response = deliveryService.createDelivery(deliveryInfo, customerAddress);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return ResponseEntity.ok("DeliveryInfo create successfully");
        } else {
            return response;
        }
    }

    @GetMapping("/find/{deliveryId}")
    public ResponseEntity<?> getDeliveryById(@PathVariable Integer deliveryId) {
        return deliveryService.getDeliveryById(deliveryId);
    }

    @PutMapping("/update/{deliveryId}")
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable Integer deliveryId, @RequestBody DeliveryInfo deliveryInfo) {
        return deliveryService.updateDeliveryStatus(deliveryId, deliveryInfo);
    }

    @DeleteMapping("/delete/{deliveryId}")
    public ResponseEntity<?> deleteDelivery(@PathVariable Integer deliveryId) {
        return deliveryService.deleteDelivery(deliveryId);
    }

    @PutMapping("/addCourier/{deliveryId}")
    public ResponseEntity<?> addCourierToDelivery(@PathVariable Integer deliveryId, @RequestBody Courier courier) {
        return deliveryService.addCourierToDelivery(deliveryId, courier);
    }

    //login and logout??
}
