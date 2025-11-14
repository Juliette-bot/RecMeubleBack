package com.backend.recMeuble.controller;

import com.backend.recMeuble.DTO.AuthenticationResponse;
import com.backend.recMeuble.configuration.JWTUtils;
import com.backend.recMeuble.entity.User;
import com.backend.recMeuble.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth: register + login
 * Endpoints: /api/v1/register, /api/v1/login
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RegistrationLoginController {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService; // ← évite le couplage à l’implémentation
    private final JWTUtils jwtUtils;

    // === DTOs ===
    public record AuthRequest(String mail, String password) {}
    public record AuthResponse(String accessToken, String role) {}
    public record RegisterResponse(Long id, String mail, String role) {}

    // -------------------- REGISTER --------------------

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody User user) {
        if (userRepository.existsByMail(user.getMail())) {
            log.warn("❌ Tentative d'inscription avec un mail déjà utilisé : {}", user.getMail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        user.setRole(User.Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);
        log.info("✅ Utilisatrice enregistrée : {} {}", saved.getFirstname(), saved.getLastname());

        // 🔐 génération du token
        String token = jwtUtils.generateToken(saved);


        AuthenticationResponse response = new AuthenticationResponse(
                token,
                saved.getMail(),
                saved.getRole().name()
        );

        return ResponseEntity.ok(response);
    }



    // -------------------- LOGIN --------------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest req) {
        try {
            // 1) Authentifie (lévera BadCredentialsException si mdp invalide)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.mail(), req.password())
            );

            // 2) Recharge depuis la BDD (source de vérité) → récupère authorities/roles
            UserDetails ud = userDetailsService.loadUserByUsername(req.mail());

            // 3) Génère le token avec claim "roles" basé sur les authorities
            String token = jwtUtils.generateToken(ud);

            // 4) Expose le rôle principal pour l’UI (optionnel mais pratique)
            String role = ud.getAuthorities().stream()
                    .map(a -> a.getAuthority())           // "ROLE_ADMIN"
                    .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a) // "ADMIN"
                    .findFirst().orElse(User.Role.USER.name());

            log.info("🔐 Connexion OK pour {}", req.mail());
            return ResponseEntity.ok(new AuthResponse(token, role));

        } catch (Exception ex) {
            log.warn("⚠️ Échec de connexion pour {} : {}", req.mail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
