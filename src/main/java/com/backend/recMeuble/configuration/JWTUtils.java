package com.backend.recMeuble.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {

    @Value("${app.secret.key}")        // Clé en Base64 (>= 32 octets une fois décodée)
    private String secretKey;

    @Value("${app.expiration-time}")   // en millisecondes (ex: 3600000 = 1h)
    private Long expirationTime;

    // ================== PUBLIC API ==================

    /** Génère un JWT (subject = username = mail). */
    // ================== PUBLIC API ==================
    public String generateToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername());
    }
    public String generateToken(String usernameOrMail) {
        return createToken(new HashMap<>(), usernameOrMail);
    }


    /** Extrait le "username" (ici : mail) d’un JWT. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Valide le JWT : même username + non expiré. */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ================== INTERNE ==================

    private String createToken(Map<String, Object> claims, String subject) {
        final Date now = new Date();
        final Date exp = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        // secretKey doit être Base64 dans application.properties
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
