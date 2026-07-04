package com.luftborn.luftborn_backend.dto.response;

public record RotatedToken(
        String newRawToken,
        Long userId
) {
}
