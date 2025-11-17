package com.backend.recMeuble.controller;

import com.backend.recMeuble.DTO.FurnitureTypeResponse;
import com.backend.recMeuble.repository.FurnitureTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/furniture-types")
@RequiredArgsConstructor
public class FurnitureTypeController {

    private final FurnitureTypeRepository furnitureTypeRepository;

    @GetMapping
    public List<FurnitureTypeResponse> getAllTypes() {
        return furnitureTypeRepository.findAll().stream()
                .map(t -> new FurnitureTypeResponse(t.getId(), t.getName()))
                .toList();
    }
}