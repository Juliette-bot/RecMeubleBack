package com.backend.recMeuble.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "furniture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Furniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private FurnitureType type;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal height;

    @Column(nullable = false)
    private BigDecimal width;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FurnitureStatus status = FurnitureStatus.PENDING_REVIEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    //@ManyToOne
    //@JoinColumn(name = "order_id")
    //private Order order;

    @ManyToOne(fetch = FetchType.LAZY) // Par défaut, @ManyToOne est EAGER. En pratique, on préfère souvent LAZY pour éviter de charger trop de choses.
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updated_at;

    @OneToMany(
            mappedBy = "furniture",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Picture> pictures = new ArrayList<>();

    public void addPicture(Picture picture) {
        pictures.add(picture);
        picture.setFurniture(this);
    }

    public void removePicture(Picture picture) {
        pictures.remove(picture);
        picture.setFurniture(null);
    }
}
