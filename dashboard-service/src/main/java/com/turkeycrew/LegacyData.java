package com.turkeycrew;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class LegacyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String area;

    @Column
    private String city;

    @Column
    private String restaurant;

    @Column
    private Integer price;

    @Column
    private Double avg_rating;

    @Column
    private Integer total_rating;

    @Column
    private String food_type;

    @Column
    private String address;

    @Column
    private Integer delivery_time;

    @Override
    public String toString() {
        return "LegacyData{" +
                "id=" + id +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                ", restaurant='" + restaurant + '\'' +
                ", price=" + price +
                ", avg_rating=" + avg_rating +
                ", total_rating=" + total_rating +
                ", food_type='" + food_type + '\'' +
                ", address='" + address + '\'' +
                ", delivery_time=" + delivery_time +
                '}';
    }
}
