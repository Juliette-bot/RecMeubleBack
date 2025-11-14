package com.backend.recMeuble.controller;


import com.backend.recMeuble.DTO.FurnitureResponse;
import com.backend.recMeuble.entity.Furniture;
import com.backend.recMeuble.entity.FurnitureStatus;
import com.backend.recMeuble.repository.FurnitureRepository;
import com.backend.recMeuble.service.FurnitureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FurnitureController {
    private final FurnitureRepository furnitureRepository;

    @GetMapping("/furniture")
    public List<FurnitureResponse> getPublishedFurniture() {

        System.out.println("Controller /api/furniture appelé");

        List<Furniture> furnitureList =
                furnitureRepository.findByStatus(FurnitureStatus.PUBLISHED);

        System.out.println("Nombre de furniture trouvés : " + furnitureList.size());

        return furnitureList.stream()
                .map(FurnitureResponse::fromEntity)
                .toList();
    }

    @GetMapping("/furniture/{id}")
    public ResponseEntity<FurnitureResponse> getFurnitureById(@PathVariable Integer id) {
        return FurnitureService.getFurnitureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
