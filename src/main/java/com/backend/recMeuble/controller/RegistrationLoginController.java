package com.backend.recMeuble.controller;

import com.backend.recMeuble.configuration.JWTUtils;
import com.backend.recMeuble.entity.User;
import com.backend.recMeuble.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class RegistrationLoginController {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    // -------------------- REGISTER --------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByMail(user.getMail())) {
            log.warn("❌ Tentative d'inscription avec un mail déjà utilisé : {}", user.getMail());
            return ResponseEntity.badRequest().body("Mail déjà utilisé");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("✅ Nouvelle utilisatrice enregistrée : {} {}", savedUser.getFirstname(), savedUser.getLastname());
        return ResponseEntity.ok(savedUser);
    }

    // -------------------- LOGIN --------------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            var dbUserOpt = userRepository.findByMail(user.getMail());
            if (dbUserOpt.isEmpty()) {
                log.warn("Login: mail introuvable en base: {}", user.getMail());
            } else {
                var dbUser = dbUserOpt.get();
                log.debug("Login: hash DB = {}", dbUser.getPassword());
                boolean matches = passwordEncoder.matches(user.getPassword(), dbUser.getPassword());
                log.debug("Login: password matches? {}", matches);
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            log.info("🔐 Connexion réussie pour l'utilisatrice : {}", user.getMail());

            // Si tu veux générer un JWT ici :
            String token = jwtUtils.generateToken(user.getMail());
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception ex) {
            log.warn("⚠️ Échec de connexion pour le mail {} : {}", user.getMail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

    }

    // -------------------- DTO de réponse --------------------
    record AuthResponse(String token) {}
}
