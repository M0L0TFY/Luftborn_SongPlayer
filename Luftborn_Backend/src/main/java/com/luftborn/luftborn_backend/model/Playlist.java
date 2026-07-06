package com.luftborn.luftborn_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "playlists", indexes = {
        @Index(name = "idx_playlist_owner", columnList = "owner_id")
})
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Required by hibernate to instantiate entities, protected to prevent calling new object()
@Builder @AllArgsConstructor(access = AccessLevel.PRIVATE) //Required by Builder, private to force builder pattern
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "total_duration_seconds", nullable = false)
    @Builder.Default //Prevents building the totalDurationSeconds with null value
    private Integer totalDurationSeconds = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private boolean isPublic = false;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("addedAt ASC") //Sort the songs in the playlist by the order they were added in
    @Builder.Default //Prevents building the playlistSongs with null values
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return id != null && Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
