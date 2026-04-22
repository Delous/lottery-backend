package ru.onexteam.lottery.security;

public class JwtUtil {

    public String generateToken(Long userId, String role) {
        return "";
    }

    public boolean validateToken(String token) {
        return true;
    }

    public Long extractUserId(String token) {
        return 0L;
    }

    public String extractRole(String token) {
        return "";
    }
}