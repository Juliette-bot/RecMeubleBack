package com.backend.recMeuble.service;

import com.backend.recMeuble.DTO.FurnitureResponse;
import com.backend.recMeuble.entity.Furniture;
import com.backend.recMeuble.entity.FurnitureStatus;
import com.backend.recMeuble.repository.FurnitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public List<FurnitureResponse> getFurnitureStatusPendingReview() {
        return furnitureRepository.findByStatus(FurnitureStatus.PENDING_REVIEW)
                .stream()
                .map(FurnitureResponse::fromEntity)
                .toList();
    }

    public FurnitureResponse updateFurnitureStatusValidated(Integer id) {

        Furniture existing = furnitureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meuble introuvable"));

                existing.setStatus(FurnitureStatus.PUBLISHED);
                Furniture saved =  furnitureRepository.save(existing);

                return FurnitureResponse.fromEntity(saved);


    }

    public FurnitureResponse updateFurnitureStatusRejected(Integer id) {

        Furniture existing = furnitureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meuble introuvable"));

        existing.setStatus(FurnitureStatus.REJECTED);

        Furniture saved = furnitureRepository.save(existing);
      return  FurnitureResponse.fromEntity(saved);

    }
}
