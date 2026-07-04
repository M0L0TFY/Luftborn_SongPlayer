package com.luftborn.luftborn_backend.service;

import com.luftborn.luftborn_backend.dto.request.LoginRequest;
import com.luftborn.luftborn_backend.dto.request.RegisterRequest;
import com.luftborn.luftborn_backend.dto.response.AuthResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface AuthService {
    AuthResult register(@Valid RegisterRequest request);
    AuthResult login(@Valid LoginRequest request);
    Optional<AuthResult> rotateToken(String oldRawRefreshToken);
    void logout(String refreshTokenFromCookie);
    ResponseCookie generateEmptyCookie();
    ResponseCookie generateCookie(String refreshToken);
}
