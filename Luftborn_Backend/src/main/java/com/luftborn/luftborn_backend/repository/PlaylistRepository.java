package com.luftborn.luftborn_backend.repository;

import com.luftborn.luftborn_backend.model.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @EntityGraph(attributePaths = "owner")
    Optional<Playlist> findWithOwnerById(Long playlistId);

    @EntityGraph(attributePaths = "owner")
    Page<Playlist> findAllByOwnerId(Long ownerId, Pageable pageable);
}
