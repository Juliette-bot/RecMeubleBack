package com.backend.recMeuble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(nullable = false)
    private String zipcode;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // 🟢 Label pour affichage côté front
    public String getDisplayLabel() {
        StringBuilder sb = new StringBuilder();

        if (streetNumber != null && !streetNumber.isBlank()) {
            sb.append(streetNumber).append(" ");
        }

        if (streetName != null && !streetName.isBlank()) {
            sb.append(streetName);
        }

        if (zipcode != null && !zipcode.isBlank()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(zipcode);
        }

        if (city != null && city.getName() != null && !city.getName().isBlank()) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(city.getName());
        }

        return sb.toString();
    }
}
