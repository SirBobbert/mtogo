package com.turkeycrew.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurant_id;
    private String name;
    private String address;
    private String phoneNumber;
    private String website;
    private String email;
    private String description;

    @OneToMany(cascade = CascadeType.ALL)  // Assuming a one-to-many relationship
    @JoinColumn(name = "restaurant_id")
    private List<MenuItem> menu;

}
