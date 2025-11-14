package com.backend.recMeuble.DTO;

public record RegisterRequest(
        String firstname,
        String lastname,
        String mail,
        String password
) {}
