package com.luftborn.luftborn_backend.service;

import com.luftborn.luftborn_backend.dto.request.PlaylistRequest;
import com.luftborn.luftborn_backend.dto.response.PlaylistResponse;
import com.luftborn.luftborn_backend.model.Playlist;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface PlaylistService {
    @PreAuthorize("#ownerId == authentication.principal.id")
    PlaylistResponse createPlaylist(Long ownerId, @Valid PlaylistRequest request);

    @PreAuthorize("#ownerId == authentication.principal.id")
    Page<PlaylistResponse> getMyPlaylists(Long ownerId, Pageable pageable);

    @PreAuthorize("@playlistSecurity.isOwnerOfPlaylist(#playlistId) or @playlistSecurity.publicPlaylist(#playlistId)")
    PlaylistResponse getPlaylistById(Long playlistId);

    @PreAuthorize("@playlistSecurity.isOwnerOfPlaylist(#playlistId)")
    PlaylistResponse updatePlaylist(Long playlistId, @Valid PlaylistRequest request);

    @PreAuthorize("@playlistSecurity.isOwnerOfPlaylist(#playlistId)")
    void deletePlaylist(Long playlistId);

    Playlist getPlaylistEntityById(Long playlistId);
}
