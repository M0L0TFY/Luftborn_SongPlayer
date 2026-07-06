package com.luftborn.luftborn_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;

/*
* Represents the playlist and songs relationship
*   (ManyToMany implemented as two ManyToOne to add other columns, and store it as a List for relative ordering and duplicates)
* Index on song_id, playlist_id so we can search limited number of songs from multiple playlists instantly
* Index on playlist_id so we can search for the user's playlist instantly from multiple playlists
*/

@Entity
@Table(name = "playlist_songs", indexes = {
        @Index(name = "idx_song_playlist", columnList = "song_id, playlist_id"),
        @Index(name = "idx_playlist", columnList = "playlist_id")}
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Required by hibernate to instantiate entities, protected to prevent calling new object()
@Builder @AllArgsConstructor(access = AccessLevel.PRIVATE) //Required by Builder, private to force builder pattern
public class PlaylistSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @CreationTimestamp
    @Column(name = "added_at", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private Instant addedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSong that = (PlaylistSong) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
