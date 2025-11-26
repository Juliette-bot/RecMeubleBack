package com.backend.recMeuble.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String mail;
    private String role;
    private Integer addressId;
    // Ne JAMAIS exposer le mot de passe
}