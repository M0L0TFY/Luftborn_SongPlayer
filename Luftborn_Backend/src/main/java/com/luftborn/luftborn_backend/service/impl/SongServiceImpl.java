package com.luftborn.luftborn_backend.service.impl;

import com.luftborn.luftborn_backend.dto.request.SongRequest;
import com.luftborn.luftborn_backend.dto.response.SongResponse;
import com.luftborn.luftborn_backend.exception.ResourceNotFoundException;
import com.luftborn.luftborn_backend.mapper.SongMapper;
import com.luftborn.luftborn_backend.model.Album;
import com.luftborn.luftborn_backend.model.Song;
import com.luftborn.luftborn_backend.model.User;
import com.luftborn.luftborn_backend.repository.SongRepository;
import com.luftborn.luftborn_backend.service.AlbumService;
import com.luftborn.luftborn_backend.service.SongService;
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
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final UserService userService;
    private final AlbumService albumService;

    @Override
    @Transactional
    public SongResponse createSong(Long artistId, SongRequest request) {
        User artist = userService.getUserEntityById(artistId);
        Song song = songMapper.toEntity(request);
        song.setArtist(artist);
        if (request.albumId() != null) {
            Album album = albumService.getAlbumEntityById(request.albumId());
            song.setAlbum(album);
        }
        return songMapper.toResponse(songRepository.save(song));
    }

    @Override
    @Transactional(readOnly = true)
    public SongResponse getSongById(Long songId) {
        return songMapper.toResponse(getSongEntityWithArtistAndAlbumById(songId));
    }

    @Override
    @Transactional
    public void deleteSong(Long songId) {
        songRepository.delete(getSongEntityById(songId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SongResponse> getAllSongsByAlbumId(Long albumId, Pageable pageable) {
        return songRepository.findByAlbumId(albumId, pageable).map(songMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SongResponse> getAllSongsByArtistUsername(String artistName, Pageable pageable) {
        return songRepository.findByArtistUsername(artistName, pageable).map(songMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Song getSongEntityById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song with id: " + songId + " not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public Song getSongEntityWithArtistById(Long songId) {
        return songRepository.findWithArtistById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song with id: " + songId + " not found."));
    }

    private Song getSongEntityWithArtistAndAlbumById(Long songId) {
        return songRepository.findWithArtistAndAlbumById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song with id: " + songId + " not found."));
    }
}
