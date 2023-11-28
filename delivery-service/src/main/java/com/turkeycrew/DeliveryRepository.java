package com.turkeycrew;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryRepository extends JpaRepository<DeliveryInfo, Integer> {
}
