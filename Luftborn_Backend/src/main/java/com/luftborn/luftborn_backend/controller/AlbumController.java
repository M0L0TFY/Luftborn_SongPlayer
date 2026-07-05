package com.luftborn.luftborn_backend.controller;

import com.luftborn.luftborn_backend.dto.request.AlbumRequest;
import com.luftborn.luftborn_backend.dto.response.AlbumResponse;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import com.luftborn.luftborn_backend.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
@Tag(name = "2. Albums")
public class AlbumController {
    private final AlbumService albumService;

    @Operation(summary = "Create Album")
    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AlbumRequest request
            ) {
        AlbumResponse response = albumService.createAlbum(userDetails.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get Album By ID")
    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumResponse> getAlbumById(@PathVariable Long albumId) {
        return ResponseEntity.ok(albumService.getAlbumById(albumId));
    }

    @Operation(summary = "Delete Album")
    @DeleteMapping("/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long albumId) {
        albumService.deleteAlbum(albumId);
        return ResponseEntity.noContent().build();
    }
}
