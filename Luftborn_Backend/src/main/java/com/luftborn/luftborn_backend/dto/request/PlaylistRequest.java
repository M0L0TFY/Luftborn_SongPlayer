package com.luftborn.luftborn_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlaylistRequest(
        @Schema(example = "Playlist #1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Playlist name required")
        @Size(min = 1, max = 50, message = "Playlist name must be between 1 and 50 characters")
        String name,

        @Size(max = 255, message = "Playlist description can't exceed 255 characters")
        String description,

        @Schema(example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isPublic
) {
}
