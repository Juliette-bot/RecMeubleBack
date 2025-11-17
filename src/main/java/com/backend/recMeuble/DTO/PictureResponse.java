package com.backend.recMeuble.DTO;

import com.backend.recMeuble.entity.Picture;

public record PictureResponse(
        Integer id,
        String url,
        String altText
) {
    public static PictureResponse fromEntity(Picture picture) {
        return new PictureResponse(
                picture.getId(),
                picture.getUrl(),
                picture.getAltText()
        );
    }
}
