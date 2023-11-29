package com.turkeycrew;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByEmail(String email);

    boolean existsByPassword(String password);

    boolean existsByFullName(String fullName);

    Optional<Customer> findByEmail(String email);

    Customer findByEmailAndPassword(String email, String password);

    boolean existsByAddress(String address);
}