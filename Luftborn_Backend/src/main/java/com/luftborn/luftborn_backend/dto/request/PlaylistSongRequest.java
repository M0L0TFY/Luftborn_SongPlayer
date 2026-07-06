package com.luftborn.luftborn_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PlaylistSongRequest(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Playlist ID required")
        Long playlistId,

        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Song ID required")
        Long songId
) {
}
