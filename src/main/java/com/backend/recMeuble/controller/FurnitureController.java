package com.backend.recMeuble.controller;

import com.backend.recMeuble.DTO.FurnitureResponse;
import com.backend.recMeuble.service.FurnitureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Service
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FurnitureController {

    private final FurnitureService furnitureService;

    @GetMapping("/furniture")
    public List<FurnitureResponse> getFurnitureOnline() {
        System.out.println("Controller /api/furniture appelé");

        List<FurnitureResponse> furnitureList = furnitureService.getPublishedFurniture(); //instencie ma classe de furnitureresponse sour forme de liste

        System.out.println("Nombre de furniture trouvés : " + furnitureList.size());

        return furnitureList;
    }


    @GetMapping("/furniture/{id}")
    public ResponseEntity<FurnitureResponse> getFurnitureById(@PathVariable Integer id) {
        return furnitureService.getFurnitureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
