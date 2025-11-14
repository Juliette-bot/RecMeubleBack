package com.backend.recMeuble.DTO;

import com.backend.recMeuble.entity.Furniture;
import java.math.BigDecimal;

public record FurnitureResponse(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        BigDecimal height,
        BigDecimal width,
        String typeName,
        String cityName,
        String zipcode,
        String status
) {
    public static FurnitureResponse fromEntity(Furniture f) {
        return new FurnitureResponse(
                f.getId(),
                f.getName(),
                f.getDescription(),
                f.getPrice(),
                f.getHeight(),
                f.getWidth(),
                f.getType() != null ? f.getType().getName() : null,
                (f.getAddress() != null && f.getAddress().getCity() != null)
                        ? f.getAddress().getCity().getName()
                        : null,
                f.getAddress() != null ? f.getAddress().getZipcode() : null,
                f.getStatus() != null ? f.getStatus().name() : null
        );
    }
}
