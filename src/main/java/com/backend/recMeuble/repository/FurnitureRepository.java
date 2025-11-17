package com.backend.recMeuble.repository;

import com.backend.recMeuble.entity.Furniture;
import com.backend.recMeuble.entity.FurnitureStatus;
import com.backend.recMeuble.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FurnitureRepository extends JpaRepository<Furniture, Integer> {

    List<Furniture> findByStatus(FurnitureStatus furnitureStatus);

    // 🟢 tous les meubles d'une utilisatrice (seller) donnée
    List<Furniture> findBySeller(User seller);


    Optional<Furniture> findByIdAndSeller(Integer id, User seller);
}
