package com.luftborn.luftborn_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "songs", indexes = {
        @Index(name = "idx_song_artist", columnList = "artist_id"),
        @Index(name = "idx_song_album", columnList = "album_id")
})
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Required by hibernate to instantiate entities, protected to prevent calling new object()
@Builder @AllArgsConstructor(access = AccessLevel.PRIVATE) //Required by Builder, private to force builder pattern
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private User artist;

    //A song can be released as a single (not associated to an album)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private Instant uploadedAt;

    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE) //Only remove the song from the playlist if the song was deleted
    @Builder.Default //Prevents building the playlistSongs with null values
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id != null && Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
