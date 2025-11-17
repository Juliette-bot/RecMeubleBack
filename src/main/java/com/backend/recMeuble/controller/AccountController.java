package com.backend.recMeuble.controller;

import com.backend.recMeuble.DTO.UserUpdateDto;
import com.backend.recMeuble.entity.User;
import com.backend.recMeuble.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final CustomUserDetailsService userService;

    @GetMapping
    public ResponseEntity<User> getCurrentUser() {
        // éventuellement renvoyer un DTO plutôt que l’entité entière
        User user = userService.getCurrentUserPublic();
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateCurrentUser(@RequestBody UserUpdateDto dto) {
        User updated = userService.updateCurrentUser(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }
}
