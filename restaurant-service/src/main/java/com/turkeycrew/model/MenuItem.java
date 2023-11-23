package com.turkeycrew.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menu_item_id;
    private String name;
    private String description;
    private double price;

    // In MenuItem entity
    @ManyToOne
    @JoinColumn(name = "restaurant_fk")
    @JsonBackReference
    private Restaurant restaurant;
}
