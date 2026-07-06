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
import com.luftborn.luftborn_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SongServiceImpl")
public class SongServiceImplTest {
    @Mock
    private SongRepository songRepository;
    @Mock
    private SongMapper songMapper;
    @Mock
    private UserService userService;
    @Mock
    private AlbumService albumService;
    @InjectMocks
    private SongServiceImpl songService;

    private User artist;
    private Album album;
    private Song song;
    private SongRequest songRequestWithAlbum;
    private SongRequest songRequestWithoutAlbum;
    private SongResponse songResponse;

    @BeforeEach
    void setup() {
        artist = User.builder()
                .username("artist")
                .email("artist@example.com")
                .hashedPassword("password")
                .build();

        album = Album.builder()
                .title("album")
                .artist(artist)
                .releaseDate(LocalDate.of(2005, 9, 2))
                .build();

        song = Song.builder()
                .title("song")
                .artist(artist)
                .album(album)
                .durationSeconds(120)
                .releaseDate(LocalDate.of(2005, 9, 2))
                .build();

        songRequestWithAlbum = new SongRequest(
                "song", 1L, 180, LocalDate.of(2005, 9, 2)
        );

        songRequestWithoutAlbum = new SongRequest(
                "song", null, 180, LocalDate.of(2005, 9, 2)
        );

        songResponse = new SongResponse(
                1L, "song", 1L, "artist", 1L, "album",
                180, LocalDate.of(2005, 9, 2), null
        );
    }

    @Nested
    class createSong {
        @Test
        @DisplayName("createSong with albumId should attach an album")
        void createSongWithAlbumId() {
            when(userService.getUserEntityById(1L)).thenReturn(artist);
            when(songMapper.toEntity(songRequestWithAlbum)).thenReturn(song);
            when(albumService.getAlbumEntityById(1L)).thenReturn(album);
            when(songRepository.save(song)).thenReturn(song);
            when(songMapper.toResponse(song)).thenReturn(songResponse);

            SongResponse response = songService.createSong(1L, songRequestWithAlbum);

            assertThat(response).isEqualTo(songResponse);
            assertThat(song.getArtist()).isEqualTo(artist);
            assertThat(song.getAlbum()).isEqualTo(album);

            verify(albumService).getAlbumEntityById(1L);
            verify(songRepository).save(song);
        }
        @Test
        @DisplayName("createSong without albumId shouldn't attach an album")
        void createSongWithoutAlbumId() {
            when(userService.getUserEntityById(1L)).thenReturn(artist);
            when(songMapper.toEntity(songRequestWithoutAlbum)).thenReturn(song);
            when(songRepository.save(song)).thenReturn(song);
            when(songMapper.toResponse(song)).thenReturn(songResponse);

            SongResponse response = songService.createSong(1L, songRequestWithoutAlbum);

            assertThat(response).isEqualTo(songResponse);
            assertThat(song.getArtist()).isEqualTo(artist);

            verify(albumService, never()).getAlbumEntityById(any());
            verify(songRepository).save(song);
        }
    }

    @Nested
    class getSongById {
        @Test
        @DisplayName("getSongById should return mapped response when song exists")
        void getSongByIdExists() {
            when(songRepository.findWithArtistAndAlbumById(1L)).thenReturn(Optional.of(song));
            when(songMapper.toResponse(song)).thenReturn(songResponse);

            SongResponse response = songService.getSongById(1L);

            assertThat(response).isEqualTo(songResponse);
        }
        @Test
        @DisplayName("getSongById should throw ResourceNotFoundException when song doesn't exist")
        void getSongByIdMissing() {
            when(songRepository.findWithArtistAndAlbumById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> songService.getSongById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");

            verify(songMapper, never()).toResponse(any());
        }

        @Nested
        class deleteSong {
            @Test
            @DisplayName("deleteSong should delete an existing song")
            void deleteSongExists() {
                when(songRepository.findById(1L)).thenReturn(Optional.of(song));

                songService.deleteSong(1L);

                verify(songRepository).delete(song);
            }
            @Test
            @DisplayName("deleteSong should throw ResourceNotFoundException when song doesn't exist")
            void deleteSongMissing() {
                when(songRepository.findById(99L)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> songService.deleteSong(99L))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("99");

                verify(songRepository, never()).delete(any());
            }
        }

    }
}
