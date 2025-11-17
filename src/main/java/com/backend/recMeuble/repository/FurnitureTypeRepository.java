package com.backend.recMeuble.repository;

import com.backend.recMeuble.entity.FurnitureType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FurnitureTypeRepository extends JpaRepository<FurnitureType, Integer> {

    Optional<FurnitureType> findByName(String name);
}
