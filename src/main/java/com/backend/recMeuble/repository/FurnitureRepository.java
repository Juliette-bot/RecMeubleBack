package com.backend.recMeuble.repository;

import com.backend.recMeuble.entity.Furniture;
import com.backend.recMeuble.entity.FurnitureStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FurnitureRepository extends JpaRepository<Furniture, Integer> {

    List<Furniture> findByStatus(FurnitureStatus furnitureStatus);
}
