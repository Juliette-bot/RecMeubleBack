package com.backend.recMeuble.DTO;

public record AuthenticationResponse(
        String token,
        String mail,
        String role
) {}
