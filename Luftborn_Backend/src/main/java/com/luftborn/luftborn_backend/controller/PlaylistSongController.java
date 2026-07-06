package com.luftborn.luftborn_backend.controller;

import com.luftborn.luftborn_backend.dto.request.PlaylistSongRequest;
import com.luftborn.luftborn_backend.dto.response.PlaylistSongResponse;
import com.luftborn.luftborn_backend.service.PlaylistSongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/playlist-songs")
@RequiredArgsConstructor
@Tag(name = "5. Playlist Songs")
public class PlaylistSongController {
    private final PlaylistSongService playlistSongService;

    @Operation(summary = "Add Song To Playlist")
    @PostMapping
    public ResponseEntity<PlaylistSongResponse> addSongToPlaylist(@Valid @RequestBody PlaylistSongRequest request) {
        PlaylistSongResponse response = playlistSongService.addSongToPlaylist(request.playlistId(), request.songId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Playlist Songs")
    @GetMapping("/{playlistId}")
    public ResponseEntity<Page<PlaylistSongResponse>> getPlaylistSongs(
            @PathVariable Long playlistId,
            @ParameterObject @PageableDefault(sort = "addedAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(playlistSongService.getPlaylistSongs(playlistId, pageable));
    }

    @Operation(summary = "Remove Song From Playlist")
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistSongService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.noContent().build();
    }
}
