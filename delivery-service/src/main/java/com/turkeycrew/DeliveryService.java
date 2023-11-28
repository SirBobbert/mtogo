package com.turkeycrew;

import lombok.AllArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.turkeycrew.DeliveryUtils.*;

@Service
@AllArgsConstructor
public class DeliveryService {

    private final CourierRepository courierRepository;
    private final DeliveryRepository deliveryRepository;

    //----------Courier functions----------
    public ResponseEntity<String> createCourier(Courier courier) {

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
        if (!isValidEmail(newEmail)) {return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid email address");}

        Optional<Courier> courierOptional = courierRepository.findById(courierId);

        if (courierOptional.isPresent()) {
            Courier courierToUpdate = courierOptional.get();

            if (newEmail == courierToUpdate.getEmail()) {return ResponseEntity.status(HttpStatus.OK).body("This email address is already yours");}

            if (courierRepository.existsByEmail(newEmail)) {return ResponseEntity.status(HttpStatus.CONFLICT).body("A courier with the email " + newEmail + " already exists");}

            courierRepository.save(courierToUpdate);
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

        deliveryInfo = deliveryRepository.save(deliveryInfo);
        System.out.println("deliveryInfo: " + deliveryInfo + " created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryInfo.toString());
    }

    public ResponseEntity<String> getDeliveryById(Integer deliveryId) {
        Optional<DeliveryInfo> deliveryOptional = deliveryRepository.findById(deliveryId);

        if (deliveryOptional.isPresent()) {
            DeliveryInfo deliveryInfo = deliveryOptional.get();
            String response = deliveryInfo.getCourier() == null ?
                    "DeliveryInfo Id: " + deliveryInfo.getId() + "\nDelivery Time: " + deliveryInfo.getDiliveryTime() + "\nAddress: " + deliveryInfo.getAddress() :
                    "DeliveryInfo Id: " + deliveryInfo.getId() + "\nDelivery Time: " + deliveryInfo.getDiliveryTime() + "\nAddress: " + deliveryInfo.getAddress() + "\nCourierId:" + deliveryInfo.getCourier();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DeliveryInfo not found with ID: " + deliveryId);
        }
    }

    public ResponseEntity<String> updateDeliveryStatus(Integer deliveryId, DeliveryInfo deliveryInfo) {
        Optional<DeliveryInfo> delivertyOptional = deliveryRepository.findById(deliveryId);
        if (delivertyOptional.isPresent()) {
            DeliveryInfo deliveryInfoToUpdate = delivertyOptional.get();

            deliveryInfoToUpdate.setStatus(deliveryInfo.isStatus());

            deliveryRepository.save(deliveryInfoToUpdate);
            return ResponseEntity.ok("Status has been updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found with ID: " + deliveryInfo.getId());
        }
    }

    public ResponseEntity<String> addCourierToDelivery(Integer deliveryId, Courier courier) {
        Optional<DeliveryInfo> delivertyOptional = deliveryRepository.findById(deliveryId);
        if (delivertyOptional.isPresent()) {
            DeliveryInfo deliveryInfoToUpdate = delivertyOptional.get();

            deliveryInfoToUpdate.setCourier(courier);

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

}
