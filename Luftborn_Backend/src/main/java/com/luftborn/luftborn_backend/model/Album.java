package com.luftborn.luftborn_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "albums", indexes = {
        @Index(name = "idx_album_artist", columnList = "artist_id")
})
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Required by hibernate to instantiate entities, protected to prevent calling new object()
@Builder @AllArgsConstructor(access = AccessLevel.PRIVATE) //Required by Builder, private to force builder pattern
public class Album {
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

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private Instant uploadedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return id != null && Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
