package com.turkeycrew;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier, Integer> {
    boolean existsByEmail(String email);

}
