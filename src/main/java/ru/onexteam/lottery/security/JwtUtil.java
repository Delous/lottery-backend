package ru.onexteam.lottery.security;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY =
            "mySuperSecretKeyForHS256Algorithm12345678910!";

    public String generateToken(Long userId, String role) {

        long now = System.currentTimeMillis();
        long exp = now + 3600000;

        return Jwts.builder()
                .setSubject(userId.toString())   // userId здесь
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            System.out.println("Token invalid: " + e.getMessage());
            return false;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = getClaims(token);
        try {
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования userId");
            return 0L;
        }
    }

    public String extractRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}