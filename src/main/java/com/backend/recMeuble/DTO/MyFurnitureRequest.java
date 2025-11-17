package com.backend.recMeuble.DTO;

import com.backend.recMeuble.entity.FurnitureStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyFurnitureRequest {

    private String name;

    private Integer typeId;        // id du FurnitureType

    private String description;

    private BigDecimal height;

    private BigDecimal width;

    private BigDecimal price;

    private Integer addressId;     // id de l'Address (optionnel)

}
