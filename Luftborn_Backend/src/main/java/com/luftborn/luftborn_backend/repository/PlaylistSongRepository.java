package com.luftborn.luftborn_backend.repository;

import com.luftborn.luftborn_backend.model.PlaylistSong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    Optional<PlaylistSong> findByPlaylistIdAndSongId(Long playlistId, Long songId);

    @EntityGraph(attributePaths = {"song", "song.artist"})
    Page<PlaylistSong> findByPlaylistId(Long playlistId, Pageable pageable);
}
