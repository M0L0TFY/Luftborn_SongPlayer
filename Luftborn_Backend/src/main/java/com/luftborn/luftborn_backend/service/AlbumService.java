package com.luftborn.luftborn_backend.service;

import com.luftborn.luftborn_backend.dto.request.AlbumRequest;
import com.luftborn.luftborn_backend.dto.response.AlbumResponse;
import com.luftborn.luftborn_backend.model.Album;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AlbumService {
    @PreAuthorize("#artistId == authentication.principal.id")
    AlbumResponse createAlbum(Long artistId, @Valid AlbumRequest request);
    AlbumResponse getAlbumById(Long albumId);
    Album getAlbumEntityById(Long albumId);

    @PreAuthorize("@albumSecurity.isArtistOfAlbum(#albumId)")
    void deleteAlbum(Long albumId);
}
