package com.backend.recMeuble.controller;

import com.backend.recMeuble.DTO.FurnitureResponse;
import com.backend.recMeuble.service.FurnitureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/furniture")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFurnitureController {

    private final FurnitureService furnitureService;

    @GetMapping("/pending_review")
        public List<FurnitureResponse> getFurniturePendingReview() {

        return furnitureService.getFurnitureStatusPendingReview();
        }


    @PutMapping("{id}/validated")

    public FurnitureResponse putFurnitureValidated(@PathVariable int id) {

        return furnitureService.updateFurnitureStatusValidated(id);
    }

    @PutMapping("{id}/rejected")

    public FurnitureResponse putFurnitureRejected(@PathVariable int id) {

        return furnitureService.updateFurnitureStatusRejected(id);
    }

    }






