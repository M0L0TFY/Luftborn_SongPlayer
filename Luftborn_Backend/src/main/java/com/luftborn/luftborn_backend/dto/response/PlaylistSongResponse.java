package com.luftborn.luftborn_backend.dto.response;

import java.time.Instant;

public record PlaylistSongResponse(
        Long songId,
        String songTitle,
        String artistName,
        Integer durationSeconds,
        Instant addedAt
) {
}
