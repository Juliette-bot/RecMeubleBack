package com.backend.recMeuble.DTO;

import com.backend.recMeuble.repository.FurnitureTypeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// DTO léger
@Data
@AllArgsConstructor
public class FurnitureTypeResponse {
    private Integer id;
    private String name;
}

