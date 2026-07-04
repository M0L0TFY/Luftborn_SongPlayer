package com.luftborn.luftborn_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "Username or Email", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Username or Email required")
        String usernameOrEmail,
        @Schema(example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password required")
        String password
) {
}
