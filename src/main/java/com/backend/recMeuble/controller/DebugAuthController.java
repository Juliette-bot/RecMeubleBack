package com.backend.recMeuble.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugAuthController {

    @GetMapping("/api/debug/me")
    public Object whoAmI() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return "No authentication in context";
        }

        return new Object() {
            public final String name = auth.getName();
            public final Object authorities = auth.getAuthorities();
        };
    }
}
