package com.backend.recMeuble.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "furniture") // ou le vrai nom de ta table
@Data // ceer les setteur et le getteur automatiquement
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Furniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
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
    private FurnitureStatus status = FurnitureStatus.PENDING_REVIEW;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    //@ManyToOne
    //@JoinColumn(name = "order_id")
    //private Order order;

    // plus tard tu pourras ajouter:
    // @ManyToOne
    // @JoinColumn(name = "seller_id", nullable = false)
    // private User seller;


    // 🟢 Ces colonnes sont gérées par ta DB :
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updated_at;

    /* ====== UserDetails ====== */



}

