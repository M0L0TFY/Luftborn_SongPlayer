package com.luftborn.luftborn_backend.repository;

import com.luftborn.luftborn_backend.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @EntityGraph(attributePaths = "artist")
    Optional<Song> findWithArtistById(Long songId);

    @EntityGraph(attributePaths = {"artist", "album"})
    Page<Song> findByAlbumId(Long albumId, Pageable pageable);

    @EntityGraph(attributePaths = {"artist", "album"})
    Page<Song> findByArtistUsername(String artistName, Pageable pageable);

    @EntityGraph(attributePaths = {"artist", "album"})
    Optional<Song> findWithArtistAndAlbumById(Long songId);
}
