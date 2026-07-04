package com.luftborn.luftborn_backend.dto.response;

public record AuthResult(
        String refreshToken,
        String accessToken,
        UserResponse user
) {
}
