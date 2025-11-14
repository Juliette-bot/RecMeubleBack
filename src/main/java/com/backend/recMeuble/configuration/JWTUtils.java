package com.backend.recMeuble.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
        import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    @Value("${app.secret.key}")        // Clé Base64 (>= 32 octets décodés)
    private String secretKey;

    @Value("${app.expiration-time}")   // en millisecondes (ex: 3600000 = 1h)
    private Long expirationTime;

    // ====== PUBLIC API ======

    /** Génère un JWT (subject = username = mail) avec claim "roles". */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Ex: authorities Spring = ["ROLE_ADMIN","ROLE_USER"] -> ["ADMIN","USER"]
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
                .collect(Collectors.toList());

        claims.put("roles", roles);

        return createToken(claims, userDetails.getUsername());
    }


    /** Extrait le "username" (ici : mail) d’un JWT. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Extrait les rôles (claim "roles") du JWT. */
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<?> raw = claims.get("roles", List.class);
        if (raw == null) return Collections.emptyList();
        return raw.stream().map(Object::toString).collect(Collectors.toList());
    }

    /** Valide le JWT : même username + non expiré. */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ====== INTERNE ======

    private String createToken(Map<String, Object> claims, String subject) {
        final Date now = new Date();
        final Date exp = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // ✅ utiliser Key, pas String
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
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
