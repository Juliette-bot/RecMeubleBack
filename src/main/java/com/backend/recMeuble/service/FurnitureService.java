package com.backend.recMeuble.service;

import com.backend.recMeuble.DTO.FurnitureResponse;
import com.backend.recMeuble.entity.FurnitureStatus;
import com.backend.recMeuble.repository.FurnitureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FurnitureService {

    private static FurnitureRepository furnitureRepository = null;

    public FurnitureService(FurnitureRepository furnitureRepository) {
        this.furnitureRepository = furnitureRepository;
    }

    // 🔹 Liste des meubles disponibles
    public List<FurnitureResponse> getPublishedFurniture() {
        return furnitureRepository.findByStatus(FurnitureStatus.PUBLISHED)
                .stream()
                .map(FurnitureResponse::fromEntity)
                .toList();
    }

    // 🔹 Meuble par ID
    public static Optional<FurnitureResponse> getFurnitureById(Integer id) {
        return furnitureRepository.findById(id)
                .map(FurnitureResponse::fromEntity);
    }
}
