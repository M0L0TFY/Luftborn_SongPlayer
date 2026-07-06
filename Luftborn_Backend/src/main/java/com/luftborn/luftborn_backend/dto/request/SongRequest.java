package com.luftborn.luftborn_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SongRequest(
        @Schema(example = "Song #1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Song title required")
        @Size(min = 1, max = 150, message = "Song title must be between 1 and 150 characters")
        String title,

        @Schema(example = "1")
        Long albumId,

        @Schema(example = "120", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Song duration required")
        @Min(value = 1, message = "Song duration must be at least 1 second")
        Integer durationSeconds,

        @Schema(example = "2005-09-02", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Song release date required")
        LocalDate releaseDate
) {
}
