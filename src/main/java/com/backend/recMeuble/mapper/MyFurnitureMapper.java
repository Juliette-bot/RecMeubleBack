package com.backend.recMeuble.mapper;

import com.backend.recMeuble.DTO.MyFurnitureResponse;
import com.backend.recMeuble.DTO.PictureResponse;
import com.backend.recMeuble.entity.Address;
import com.backend.recMeuble.entity.Furniture;
import com.backend.recMeuble.entity.FurnitureType;
import com.backend.recMeuble.entity.User;

import java.util.List;

public class MyFurnitureMapper {

    public static MyFurnitureResponse toResponse(Furniture furniture) {

        FurnitureType type = furniture.getType();
        Address address = furniture.getAddress();
        User seller = furniture.getSeller();

        return MyFurnitureResponse.builder()
                .id(furniture.getId())
                .name(furniture.getName())
                .description(furniture.getDescription())
                .height(furniture.getHeight())
                .width(furniture.getWidth())
                .price(furniture.getPrice())
                .status(furniture.getStatus())

                .typeId(type != null ? type.getId() : null)
                .typeName(type != null ? type.getName() : null) // adapte si ce n’est pas "getName()"

                .addressId(address != null ? address.getId() : null)
                .addressLabel(address != null ? address.getDisplayLabel() : null)


                .sellerId(seller != null ? seller.getId() : null)
                .sellerFirstname(seller != null ? seller.getFirstname() : null)
                .sellerLastname(seller != null ? seller.getLastname() : null)

                .createdAt(furniture.getCreated_at())
                .updatedAt(furniture.getUpdated_at())
                .pictures(
                        furniture.getPictures() == null
                                ? List.of()
                                : furniture.getPictures().stream()
                                .map(PictureResponse::fromEntity)
                                .toList()
                )
                .build();
    }
}
