package com.luftborn.luftborn_backend.service.impl;

import com.luftborn.luftborn_backend.dto.request.PlaylistRequest;
import com.luftborn.luftborn_backend.dto.response.PlaylistResponse;
import com.luftborn.luftborn_backend.exception.ResourceNotFoundException;
import com.luftborn.luftborn_backend.mapper.PlaylistMapper;
import com.luftborn.luftborn_backend.model.Playlist;
import com.luftborn.luftborn_backend.model.User;
import com.luftborn.luftborn_backend.repository.PlaylistRepository;
import com.luftborn.luftborn_backend.service.PlaylistService;
import com.luftborn.luftborn_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final UserService userService;

    @Override
    @Transactional
    public PlaylistResponse createPlaylist(Long ownerId, PlaylistRequest request) {
        User owner = userService.getUserEntityById(ownerId);
        Playlist playlist = playlistMapper.toEntity(request);
        playlist.setOwner(owner);
        return playlistMapper.toResponse(playlistRepository.save(playlist));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaylistResponse> getMyPlaylists(Long ownerId, Pageable pageable) {
        return playlistRepository.findAllByOwnerId(ownerId, pageable).map(playlistMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PlaylistResponse getPlaylistById(Long playlistId) {
        return playlistMapper.toResponse(getPlaylistEntityWithOwnerById(playlistId));
    }

    @Override
    @Transactional
    public PlaylistResponse updatePlaylist(Long playlistId, PlaylistRequest request) {
        Playlist playlist = getPlaylistEntityWithOwnerById(playlistId);
        playlist.setName(request.name());
        playlist.setDescription(request.description());
        playlist.setPublic(request.isPublic());
        return playlistMapper.toResponse(playlist);
    }

    @Override
    @Transactional
    public void deletePlaylist(Long playlistId) {
        playlistRepository.delete(getPlaylistEntityById(playlistId));
    }

    @Override
    @Transactional(readOnly = true)
    public Playlist getPlaylistEntityById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with id: " + playlistId + " not found."));
    }

    private Playlist getPlaylistEntityWithOwnerById(Long playlistId) {
        return playlistRepository.findWithOwnerById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with id: " + playlistId + " not found."));
    }
}
