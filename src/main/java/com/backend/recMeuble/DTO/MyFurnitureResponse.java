package com.backend.recMeuble.DTO;

import com.backend.recMeuble.entity.FurnitureStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MyFurnitureResponse {

    private Integer id;

    private String name;

    private String description;

    private BigDecimal height;

    private BigDecimal width;

    private BigDecimal price;

    private FurnitureStatus status;

    private Integer typeId;
    private String typeName; // si ton FurnitureType a un champ "name" par ex.

    private Integer addressId;
    private String addressLabel; // à adapter selon tes champs d'Address

    private Long sellerId;
    private String sellerFirstname;
    private String sellerLastname;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<PictureResponse> pictures;
}
