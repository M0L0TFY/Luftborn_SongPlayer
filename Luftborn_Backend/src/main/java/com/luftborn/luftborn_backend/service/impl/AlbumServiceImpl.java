package com.luftborn.luftborn_backend.service.impl;

import com.luftborn.luftborn_backend.dto.request.AlbumRequest;
import com.luftborn.luftborn_backend.dto.response.AlbumResponse;
import com.luftborn.luftborn_backend.exception.ResourceNotFoundException;
import com.luftborn.luftborn_backend.mapper.AlbumMapper;
import com.luftborn.luftborn_backend.model.Album;
import com.luftborn.luftborn_backend.model.User;
import com.luftborn.luftborn_backend.repository.AlbumRepository;
import com.luftborn.luftborn_backend.service.AlbumService;
import com.luftborn.luftborn_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final UserService userService;


    @Override
    @Transactional
    public AlbumResponse createAlbum(Long artistId, AlbumRequest request) {
        User artist = userService.getUserEntityById(artistId);
        Album album = albumMapper.toEntity(request);
        album.setArtist(artist);
        return albumMapper.toResponse(albumRepository.save(album));
    }

    @Override
    @Transactional(readOnly = true)
    public AlbumResponse getAlbumById(Long albumId) {
        return albumMapper.toResponse(getAlbumEntityWithArtistById(albumId));
    }

    @Override
    @Transactional
    public void deleteAlbum(Long albumId) {
        albumRepository.delete(getAlbumEntityById(albumId));
    }

    @Override
    @Transactional(readOnly = true)
    public Album getAlbumEntityById(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album with id: " + albumId + " not found."));
    }

    private Album getAlbumEntityWithArtistById(Long albumId) {
        return albumRepository.findWithArtistById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album with id: " + albumId + " not found."));
    }
}
