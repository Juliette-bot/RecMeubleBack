package com.backend.recMeuble.service;

import com.backend.recMeuble.DTO.FurnitureResponse;
import com.backend.recMeuble.entity.FurnitureStatus;
import com.backend.recMeuble.repository.FurnitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FurnitureService {

    // 👉 plus static !
    private final FurnitureRepository furnitureRepository;

    // 🔹 Liste des meubles publiés
    public List<FurnitureResponse> getPublishedFurniture() {
        return furnitureRepository.findByStatus(FurnitureStatus.PUBLISHED)
                .stream()
                .map(FurnitureResponse::fromEntity)
                .toList();
    }

    // 🔹 Meuble par ID
    public Optional<FurnitureResponse> getFurnitureById(Integer id) {
        return furnitureRepository.findById(id)
                .map(FurnitureResponse::fromEntity);
    }
}
