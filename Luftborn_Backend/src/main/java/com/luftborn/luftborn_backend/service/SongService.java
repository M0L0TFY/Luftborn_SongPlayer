package com.luftborn.luftborn_backend.service;

import com.luftborn.luftborn_backend.dto.request.SongRequest;
import com.luftborn.luftborn_backend.dto.response.SongResponse;
import com.luftborn.luftborn_backend.model.Song;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SongService {
    @PreAuthorize("#artistId == authentication.principal.id and (#request.albumId() == null or @albumSecurity.isArtistOfAlbum(#request.albumId()))")
    SongResponse createSong(Long artistId, @Valid SongRequest request);

    SongResponse getSongById(Long songId);

    Page<SongResponse> getAllSongsByAlbumId(Long albumId, Pageable pageable);

    Page<SongResponse> getAllSongsByArtistUsername(String artistName, Pageable pageable);

    @PreAuthorize("@songSecurity.isArtistOfSong(#songId)")
    void deleteSong(Long songId);

    Song getSongEntityById(Long songId);

    Song getSongEntityWithArtistById(Long songId);
}
