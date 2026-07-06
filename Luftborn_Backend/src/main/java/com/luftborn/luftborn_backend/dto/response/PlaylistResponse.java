package com.luftborn.luftborn_backend.dto.response;

import com.luftborn.luftborn_backend.model.PlaylistSong;

import java.util.List;

public record PlaylistResponse(
        Long id,
        String name,
        String description,
        Integer totalDurationSeconds,
        Long ownerId,
        String ownerUsername,
        boolean isPublic
) {
}
