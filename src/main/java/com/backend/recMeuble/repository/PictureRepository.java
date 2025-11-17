package com.backend.recMeuble.repository;

import com.backend.recMeuble.entity.Furniture;
import com.backend.recMeuble.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Integer> {
    List<Picture> findByFurniture(Furniture furniture);
}
