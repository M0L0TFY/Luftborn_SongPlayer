package com.luftborn.luftborn_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String email,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Instant createdAt
) {
}
