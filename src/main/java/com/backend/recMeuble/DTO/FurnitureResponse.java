package com.backend.recMeuble.DTO;

import com.backend.recMeuble.entity.Furniture;
import java.math.BigDecimal;
import java.util.List;

public record FurnitureResponse(
        Integer id,
        String name,
        String description,
        BigDecimal height,
        BigDecimal width,
        BigDecimal price,
        String status,
        List<PictureResponse> pictures
) {
    public static FurnitureResponse fromEntity(Furniture furniture) {
        return new FurnitureResponse(
                furniture.getId(),
                furniture.getName(),
                furniture.getDescription(),
                furniture.getHeight(),
                furniture.getWidth(),
                furniture.getPrice(),
                furniture.getStatus().name(),
                furniture.getPictures() != null
                        ? furniture.getPictures().stream()
                        .map(PictureResponse::fromEntity)
                        .toList()
                        : List.of()
        );
    }
}
