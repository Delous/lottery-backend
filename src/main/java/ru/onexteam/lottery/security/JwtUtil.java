package ru.onexteam.lottery.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.Claims;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "mySuperSecretKeyForHS256Algorithm12345678910!";
    // Минимум 32 символа (Изменить ключ)

    public String generateToken(Long userId, String role) {
        String StrUserId = userId.toString();
        long now = System.currentTimeMillis(); // время выдачи
        long exp = now + 3600000; // 1 час // на какое кол-во времени?

        String token = Jwts.builder()
                .setSubject(StrUserId)                 // ID пользователя
                .claim("role", role)                // Кастомные данные
                .setIssuedAt(new Date(now))            // Время выдачи
                .setExpiration(new Date(exp))          // Время истечения
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Подпись
                .compact();                            // Финальная сборка
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Токен просрочен");
            return false;
        } catch (SignatureException e) {
            System.out.println("Неверная подпись");
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Некорректный формат токена");
            return false;
        } catch (JwtException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
            return false;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = getClaims(token);
        if(claims != null){
            try {
                return Long.parseLong(claims.getId());
            }catch (NumberFormatException e){
                System.out.println("Ошибка преобразования userId в Long");
            }
        }
        return 0L;
    }

    public String extractRole(String token) {
        Claims claims = getClaims(token);
        if(claims != null){
            return claims.get("role", String.class);
        }
        return "";
    }

    private Claims getClaims(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e){
            System.out.println("Невалидное значение токена");
            return null;
        }
    }
}