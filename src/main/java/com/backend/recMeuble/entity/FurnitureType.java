package com.backend.recMeuble.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FurnitureType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false,unique = true)
    private String name;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updated_at;

}
