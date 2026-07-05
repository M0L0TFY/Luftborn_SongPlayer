package com.luftborn.luftborn_backend.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record AlbumResponse(
        Long id,
        String title,
        Long artistId,
        String artistName,
        LocalDate releaseDate,
        Instant uploadedAt
) {
}
