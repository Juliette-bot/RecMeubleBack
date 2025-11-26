package com.backend.recMeuble.controller;

import com.backend.recMeuble.DTO.PasswordChangeDto;
import com.backend.recMeuble.DTO.UserProfileDto;
import com.backend.recMeuble.DTO.UserUpdateDto;
import com.backend.recMeuble.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final CustomUserDetailsService userService;

    /**
     * GET /api/user/profile
     * Récupère le profil de l'utilisateur connecté
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile() {
        UserProfileDto profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    /**
     * PUT /api/user/profile
     * Met à jour les informations du profil
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UserUpdateDto dto) {
        UserProfileDto updatedProfile = userService.updateCurrentUser(dto);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * PUT /api/user/password
     * Change le mot de passe
     */
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody PasswordChangeDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }

    /**
     * DELETE /api/user/account
     * Supprime le compte de l'utilisateur
     */
    @DeleteMapping("/account")
    public ResponseEntity<Map<String, String>> deleteAccount() {
        userService.deleteCurrentUser();
        return ResponseEntity.ok(Map.of("message", "Compte supprimé avec succès"));
    }
}