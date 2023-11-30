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

    //----------Courier functions----------

//    @KafkaListener(topics = "test12", groupId = "courier-group")
//    public void listen2(ConsumerRecord<String, String> record) {
//        System.out.println("Received message from Kafka:");
//        System.out.println(String.valueOf(record.value()));
//        System.out.println("Received message from Kafka:");
//
//    }
//
//    //  Get an object from the message payload "filtered"
//    @KafkaListener(topics = "test123", groupId = "courier-group")
//    public void listen(ConsumerRecord<String, String> record) {
//        System.out.println("Received message from Kafka:");
//
//        String jsonString = record.value();
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> payload = objectMapper.readValue(jsonString, new TypeReference<>() {
//            });
//
////          Iterate over the entries in the message
//            for (Map.Entry<String, Object> entry : payload.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
//
////           Accessing specific fields
//            Object itemsObject = payload.get("items");
//
//            if (itemsObject instanceof List) {
//                List<Map<String, Object>> itemsList = (List<Map<String, Object>>) itemsObject;
//
//                for (Map<String, Object> item : itemsList) {
//                    System.out.println("Item details:");
//                    System.out.println();
//
//                    for (Map.Entry<String, Object> entry : item.entrySet()) {
//                        System.out.println(entry.getKey() + ": " + entry.getValue());
//                    }
//                }
//            }
//
//
//        } catch (IOException e) {
//            // Handle the exception, e.g., log it or throw a custom exception
//            e.printStackTrace();
//        }
//    }

//    Get an object from the message payload "unfiltered"
//    @KafkaListener(topics = "test123", groupId = "courier-group")
//    public void test(ConsumerRecord<String, String> record) {
//        System.out.println("Received message from Kafka:");
//        System.out.println(record.value());
//    }


    public ResponseEntity<String> createCourier(Courier courier) {

        if (!isValidEmail(courier.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
        }

        if (courierRepository.existsByEmail(courier.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with the email " + courier.getEmail() + " already exists");
        }

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

            courierRepository.save(updatedCourier);
            return ResponseEntity.ok("Courier updated successfully");

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

    public ResponseEntity<String> updateDeliveryStatus(Integer deliveryId, DeliveryInfo deliveryInfo) {
        Optional<DeliveryInfo> delivertyOptional = deliveryRepository.findById(deliveryId);
        if (delivertyOptional.isPresent()) {
            DeliveryInfo deliveryInfoToUpdate = delivertyOptional.get();

            deliveryInfoToUpdate.setDelivery(LocalDateTime.now());
            deliveryInfoToUpdate.setStatus(deliveryInfo.isStatus());

            deliveryRepository.save(deliveryInfoToUpdate);
            return ResponseEntity.ok("Status has been updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found with ID: " + deliveryInfo.getId());
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

            return ResponseEntity.ok("Courier has been added to delivery");
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

    @KafkaListener(topics = "penis", groupId = "delivery-group")
    public void listen2(String message) {
        System.out.println("Received message from Kafka:");
        System.out.println(message);
        kafkaTemplate.send("newPenis", getAllDeliveries());
    }


}
