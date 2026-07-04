package com.luftborn.luftborn_backend.controller;

import com.luftborn.luftborn_backend.dto.request.LoginRequest;
import com.luftborn.luftborn_backend.dto.request.RegisterRequest;
import com.luftborn.luftborn_backend.dto.response.AuthResponse;
import com.luftborn.luftborn_backend.dto.response.AuthResult;
import com.luftborn.luftborn_backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        AuthResult result = authService.register(request);
        ResponseCookie cookie = authService.generateCookie(result.refreshToken());
        AuthResponse response = AuthResponse.of(result.accessToken(), result.user());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authService.login(request);
        ResponseCookie cookie = authService.generateCookie(result.refreshToken());
        AuthResponse response = AuthResponse.of(result.accessToken(), result.user());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @Operation(summary = "Refresh Token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> rotateToken(
            @CookieValue(name = "${security.jwt.cookie-name}", required = false)
            String oldRefreshToken
    ) {
        return authService.rotateToken(oldRefreshToken)
                .map(result -> {
                    ResponseCookie cookie = authService.generateCookie(result.refreshToken());
                    AuthResponse response = AuthResponse.of(result.accessToken(), result.user());
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "${security.jwt.cookie-name}", required = false)
            String refreshToken
    ) {
        authService.logout(refreshToken);
        ResponseCookie emptyCookie = authService.generateEmptyCookie();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, emptyCookie.toString())
                .build();

    }
}
