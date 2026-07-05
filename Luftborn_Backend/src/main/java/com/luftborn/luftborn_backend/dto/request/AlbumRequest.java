package com.luftborn.luftborn_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AlbumRequest(
        @Schema(example = "Album #1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Album title required")
        @Size(min = 1, max = 150, message = "Album title must be between 1 and 150 characters")
        String title,

        @Schema(example = "2005-09-02", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Album release date required")
        LocalDate releaseDate
) {
}
