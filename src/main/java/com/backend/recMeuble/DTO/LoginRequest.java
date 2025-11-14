package com.backend.recMeuble.DTO;


public record LoginRequest(
        String mail,
        String password
) {}
