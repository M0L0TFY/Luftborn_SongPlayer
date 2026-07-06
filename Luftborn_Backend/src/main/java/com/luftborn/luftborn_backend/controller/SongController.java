package com.luftborn.luftborn_backend.controller;

import com.luftborn.luftborn_backend.dto.request.SongRequest;
import com.luftborn.luftborn_backend.dto.response.SongResponse;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import com.luftborn.luftborn_backend.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
@Tag(name = "3. Songs")
public class SongController {
    private final SongService songService;

    @Operation(summary = "Create Song")
    @PostMapping
    public ResponseEntity<SongResponse> createSong(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody SongRequest request
    ) {
        SongResponse response = songService.createSong(userDetails.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get Song By ID")
    @GetMapping("/{songId}")
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long songId) {
        return ResponseEntity.ok(songService.getSongById(songId));
    }

    @Operation(summary = "Get All Songs By Album ID")
    @GetMapping("/albums/{albumId}")
    public ResponseEntity<Page<SongResponse>> getAllSongsByAlbumId(
            @PathVariable Long albumId,
            @ParameterObject @PageableDefault(sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getAllSongsByAlbumId(albumId, pageable));
    }

    @Operation(summary = "Get All Songs By Artist Username")
    @GetMapping("/artists/{artistName}")
    public ResponseEntity<Page<SongResponse>> getAllSongsByArtistUsername(
            @PathVariable String artistName,
            @ParameterObject @PageableDefault(sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getAllSongsByArtistUsername(artistName, pageable));
    }

    @Operation(summary = "Delete Song")
    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long songId) {
        songService.deleteSong(songId);
        return ResponseEntity.noContent().build();
    }
}
