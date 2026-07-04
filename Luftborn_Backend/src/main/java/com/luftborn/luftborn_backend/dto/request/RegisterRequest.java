package com.luftborn.luftborn_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Schema(example = "username", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,
    @Schema(example = "email@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email @NotBlank(message = "Email required")
    String email,
    @Schema(example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password required") @Size(min = 6, message = "Password must contain at least 6 characters")
    String password
) {
}
