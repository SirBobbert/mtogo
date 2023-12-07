package com.turkeycrew;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.turkeycrew.DeliveryUtils.isValidEmail;

@Service
public class DeliveryService {

    private final CourierRepository courierRepository;
    private final DeliveryRepository deliveryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DeliveryService(CourierRepository courierRepository, DeliveryRepository deliveryRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.courierRepository = courierRepository;
        this.deliveryRepository = deliveryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "createDeliveryByUserId", groupId = "delivery-group")
    public void listen(String message) {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setAddress(message);
        createDelivery(deliveryInfo);

        Optional<DeliveryInfo> updateDelivery = deliveryRepository.findById(deliveryInfo.getId());

        if (updateDelivery.isPresent()) {
            kafkaTemplate.send("updateOrderByDeliveryId", updateDelivery.get().getId());
        }
    }


    public ResponseEntity<String> createCourier(Courier courier) {

        if (!isValidEmail(courier.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
        }

        if (courierRepository.existsByEmail(courier.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the email " + courier.getEmail() + " already exists");
        }

        courier.setAvailable(true);
        courier = courierRepository.save(courier);
        System.out.println("Customer: " + courier + " created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(courier.toString());
    }

    public ResponseEntity<String> getCourierById(Integer courierId) {
        Optional<Courier> courierOptional = courierRepository.findById(courierId);

        if (courierOptional.isPresent()) {
            Courier courier = courierOptional.get();

            String response = "Courier Id: " + courier.getId() + "\nEmail: " + courier.getEmail() + "\nAvailability: " + courier.isAvailable();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courier not found with ID: " + courierId);
        }
    }

    public ResponseEntity<String> updateCourierEmail(Integer courierId, String newEmail) {

        if (!isValidEmail(newEmail)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid email address");
        }

        if (courierRepository.existsByEmail(newEmail)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the email " + newEmail + " already exists");
        }

        Optional<Courier> courierOptional = courierRepository.findById(courierId);

        if (courierOptional.isPresent()) {

            Courier updatedCourier = courierOptional.get();
            updatedCourier.setEmail(newEmail);

            Courier response = courierRepository.save(updatedCourier);
            return ResponseEntity.ok("Courier updated successfully" + "\nnew Email: " + response.getEmail());

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courier not found with ID: " + courierId);
        }
    }

    public ResponseEntity<?> deleteCourier(Integer courierId) {
        Optional<Courier> customerOptional = courierRepository.findById(courierId);

        if (customerOptional.isPresent()) {
            courierRepository.deleteById(courierId);
            return ResponseEntity.ok("Courier deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courier not found with ID: " + courierId);
        }
    }


    //----------Delivery functions----------
    public ResponseEntity<String> createDelivery(DeliveryInfo deliveryInfo) {

        // TODO: Set deliveryAddress to the address from the message payload
        deliveryInfo.setCreationTime(LocalDateTime.now());
        deliveryInfo = deliveryRepository.save(deliveryInfo);
        System.out.println("deliveryInfo: " + deliveryInfo + " created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryInfo.toString());
    }


    public ResponseEntity<String> getDeliveryById(Integer deliveryId) {
        Optional<DeliveryInfo> deliveryOptional = deliveryRepository.findById(deliveryId);

        if (deliveryOptional.isPresent()) {
            DeliveryInfo deliveryInfo = deliveryOptional.get();
            String response = deliveryInfo.getCourier() == null ?
                    "DeliveryInfo Id: " + deliveryInfo.getId() + "\nDelivery Time: " + deliveryInfo.getCreationTime() + "\nAddress: " + deliveryInfo.getAddress() :
                    "DeliveryInfo Id: " + deliveryInfo.getId() + "\nDelivery Time: " + deliveryInfo.getCreationTime() + "\nAddress: " + deliveryInfo.getAddress() + "\nCourierId:" + deliveryInfo.getCourier();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DeliveryInfo not found with ID: " + deliveryId);
        }
    }

    public ResponseEntity<String> updateDeliveryStatus(Integer deliveryId) {
        Optional<DeliveryInfo> delivertyOptional = deliveryRepository.findById(deliveryId);
        if (delivertyOptional.isPresent()) {
            DeliveryInfo deliveryInfoToUpdate = delivertyOptional.get();

            deliveryInfoToUpdate.setDelivery(LocalDateTime.now());
            deliveryInfoToUpdate.setStatus(true);

            deliveryRepository.save(deliveryInfoToUpdate);
            return ResponseEntity.ok("Status has been updated. \nNew status: " + deliveryInfoToUpdate.isStatus());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found with ID: " + deliveryId);
        }
    }

    public ResponseEntity<String> addCourierToDelivery(Integer deliveryId, Courier courier) {
        Optional<DeliveryInfo> delivertyOptional = deliveryRepository.findById(deliveryId);
        Optional<Courier> courierOptional = courierRepository.findById(courier.getId());

        if (delivertyOptional.isPresent() && courierOptional.isPresent()) {

            DeliveryInfo deliveryInfoToUpdate = delivertyOptional.get();
            Courier courierToUpdate = courierOptional.get();

            courierToUpdate.setAvailable(false);
            deliveryInfoToUpdate.setCourier(courier);

            courierRepository.save(courierToUpdate);
            deliveryRepository.save(deliveryInfoToUpdate);

            return ResponseEntity.ok("Courier has been added to delivery \nCourierId: " + courier.getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found with ID: " + deliveryId);
        }
    }

    public ResponseEntity<?> deleteDelivery(Integer deliveryId) {
        Optional<DeliveryInfo> deliveryOptional = deliveryRepository.findById(deliveryId);

        if (deliveryOptional.isPresent()) {
            deliveryRepository.deleteById(deliveryId);
            return ResponseEntity.ok("Delivery deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found with ID: " + deliveryId);
        }
    }

    public List<DeliveryInfo> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @KafkaListener(topics = "GetAllDeliveriesTrigger", groupId = "delivery-group")
    public void getAllDeliveriesTrigger(String message) {
        System.out.println("Received message from Kafka:");
        System.out.println(message);
        kafkaTemplate.send("GetAllDeliveries", getAllDeliveries());
    }


}
