package com.luftborn.luftborn_backend.controller;

import com.luftborn.luftborn_backend.dto.request.PlaylistRequest;
import com.luftborn.luftborn_backend.dto.response.PlaylistResponse;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import com.luftborn.luftborn_backend.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/playlists")
@RequiredArgsConstructor
@Tag(name = "4. Playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Operation(summary = "Create Playlist")
    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody PlaylistRequest request
    ) {
        PlaylistResponse response = playlistService.createPlaylist(userDetails.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get My Playlists")
    @GetMapping
    public ResponseEntity<Page<PlaylistResponse>> getMyPlaylists(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ParameterObject @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(playlistService.getMyPlaylists(userDetails.id(), pageable));
    }

    @Operation(summary = "Get Playlist By ID")
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> getPlaylistById(@PathVariable Long playlistId) {
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId));
    }

    @Operation(summary = "Update Playlist")
    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> updatePlaylist(@PathVariable Long playlistId, @Valid @RequestBody PlaylistRequest request) {
        return ResponseEntity.ok(playlistService.updatePlaylist(playlistId, request));
    }

    @Operation(summary = "Delete Playlist")
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}
