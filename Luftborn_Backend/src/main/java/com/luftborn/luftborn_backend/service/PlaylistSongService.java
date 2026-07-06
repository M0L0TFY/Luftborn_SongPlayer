package com.luftborn.luftborn_backend.service;

import com.luftborn.luftborn_backend.dto.response.PlaylistSongResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface PlaylistSongService {
    @PreAuthorize("@playlistSecurity.isOwnerOfPlaylist(#playlistId)")
    PlaylistSongResponse addSongToPlaylist(Long playlistId, Long songId);

    @PreAuthorize("@playlistSecurity.isOwnerOfPlaylist(#playlistId) or @playlistSecurity.publicPlaylist(#playlistId)")
    Page<PlaylistSongResponse> getPlaylistSongs(Long playlistId, Pageable pageable);

    @PreAuthorize("@playlistSecurity.isOwnerOfPlaylist(#playlistId)")
    void removeSongFromPlaylist(Long playlistId, Long songId);
}
