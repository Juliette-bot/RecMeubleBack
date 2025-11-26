package com.backend.recMeuble.service;

import com.backend.recMeuble.DTO.PasswordChangeDto;
import com.backend.recMeuble.DTO.UserProfileDto;
import com.backend.recMeuble.DTO.UserUpdateDto;
import com.backend.recMeuble.entity.User;
import com.backend.recMeuble.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(
            UsersRepository userRepository,
            @Lazy PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // ---------- Authentification (utilisé par Spring Security) ----------
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));
    }

    // ---------- Récupérer l'utilisateur connecté ----------
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Aucun utilisateur connecté");
        }

        String email = auth.getName();
        return userRepository.findByMail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé : " + email));
    }

    // ---------- Récupérer le profil (DTO sans password) ----------
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile() {
        User user = getCurrentUser();
        return mapToProfileDto(user);
    }

    // ---------- Mise à jour des informations ----------
    @Transactional
    public UserProfileDto updateCurrentUser(UserUpdateDto dto) {
        User user = getCurrentUser();

        // Vérifier si l'email est déjà utilisé par un autre compte
        if (dto.getMail() != null && !dto.getMail().equals(user.getMail())) {
            if (userRepository.existsByMail(dto.getMail())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Cet email est déjà utilisé"
                );
            }
            user.setMail(dto.getMail());
        }

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            user.setFirstname(dto.getFirstName());
        }

        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            user.setLastname(dto.getLastName());
        }

        if (dto.getAddressId() != null) {
            user.setAddress_id(dto.getAddressId());
        }

        User savedUser = userRepository.save(user);
        return mapToProfileDto(savedUser);
    }

    // ---------- Changement de mot de passe ----------
    @Transactional
    public void changePassword(PasswordChangeDto dto) {
        User user = getCurrentUser();

        // Vérifier que le mot de passe actuel est correct
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le mot de passe actuel est incorrect"
            );
        }

        // Vérifier que les nouveaux mots de passe correspondent
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Les nouveaux mots de passe ne correspondent pas"
            );
        }

        // Encoder et sauvegarder le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    // ---------- Suppression du compte ----------
    @Transactional
    public void deleteCurrentUser() {
        User user = getCurrentUser();

        // Option 1 : Suppression réelle
        userRepository.delete(user);

        // Option 2 : Soft delete (si tu as un champ isActive)
        // user.setActive(false);
        // userRepository.save(user);
    }

    // ---------- Mapper privé ----------
    private UserProfileDto mapToProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .mail(user.getMail())
                .role(user.getRole().name())
                .addressId(user.getAddress_id())
                .build();
    }
}