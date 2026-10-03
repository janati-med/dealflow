package de.janati.dealflow.auth;

public record TokenResponse(String token, String role, long expiresInSeconds) {
}