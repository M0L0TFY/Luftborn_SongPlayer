package com.luftborn.luftborn_backend.repository;

import com.luftborn.luftborn_backend.model.Album;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @EntityGraph(attributePaths = "artist")
    Optional<Album> findWithArtistById(Long albumId);
}
