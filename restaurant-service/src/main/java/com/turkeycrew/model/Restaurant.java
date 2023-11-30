package com.turkeycrew.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;  // Updated to follow JavaBean naming conventions

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Website is required")
    private String website;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Description is required")
    private String description;

    // In Restaurant entity
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<MenuItem> menu;



}
