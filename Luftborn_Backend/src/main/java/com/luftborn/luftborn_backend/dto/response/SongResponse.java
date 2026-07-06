package com.luftborn.luftborn_backend.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record SongResponse(
        Long id,
        String title,
        Long artistId,
        String artistName,
        Long albumId,
        String albumTitle,
        Integer durationSeconds,
        LocalDate releaseDate,
        Instant uploadedAt
) {
}
