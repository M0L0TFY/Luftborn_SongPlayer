package com.luftborn.luftborn_backend.service.impl;

import com.luftborn.luftborn_backend.dto.response.PlaylistSongResponse;
import com.luftborn.luftborn_backend.exception.ResourceNotFoundException;
import com.luftborn.luftborn_backend.mapper.PlaylistSongMapper;
import com.luftborn.luftborn_backend.model.Playlist;
import com.luftborn.luftborn_backend.model.PlaylistSong;
import com.luftborn.luftborn_backend.model.Song;
import com.luftborn.luftborn_backend.repository.PlaylistSongRepository;
import com.luftborn.luftborn_backend.service.PlaylistService;
import com.luftborn.luftborn_backend.service.PlaylistSongService;
import com.luftborn.luftborn_backend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class PlaylistSongServiceImpl implements PlaylistSongService {
    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistSongMapper playlistSongMapper;
    private final SongService songService;
    private final PlaylistService playlistService;

    @Override
    @Transactional
    public PlaylistSongResponse addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistService.getPlaylistEntityById(playlistId);
        Song song = songService.getSongEntityWithArtistById(songId);
        playlist.setTotalDurationSeconds(playlist.getTotalDurationSeconds() + song.getDurationSeconds());
        PlaylistSong playlistSong = playlistSongMapper.toEntity(playlist, song);
        return playlistSongMapper.toResponse(playlistSongRepository.save(playlistSong));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaylistSongResponse> getPlaylistSongs(Long playlistId, Pageable pageable) {
        return playlistSongRepository.findByPlaylistId(playlistId, pageable).map(playlistSongMapper::toResponse);
    }

    @Override
    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistService.getPlaylistEntityById(playlistId);
        Song song = songService.getSongEntityById(songId);
        playlist.setTotalDurationSeconds(playlist.getTotalDurationSeconds() - song.getDurationSeconds());
        playlistSongRepository.delete(getPlaylistSongEntityById(playlistId, songId));
    }

    private PlaylistSong getPlaylistSongEntityById(Long playlistId, Long songId) {
        return playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with id: " + playlistId + " not found."));
    }
}
