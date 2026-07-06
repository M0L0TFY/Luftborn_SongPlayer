package com.luftborn.luftborn_backend.mapper;

import com.luftborn.luftborn_backend.dto.response.PlaylistSongResponse;
import com.luftborn.luftborn_backend.model.Playlist;
import com.luftborn.luftborn_backend.model.PlaylistSong;
import com.luftborn.luftborn_backend.model.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistSongMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playlist", source = "playlist")
    @Mapping(target = "song", source = "song")
    @Mapping(target = "addedAt", ignore = true)
    PlaylistSong toEntity(Playlist playlist, Song song);

    @Mapping(target = "songId", source = "song.id")
    @Mapping(target = "songTitle", source = "song.title")
    @Mapping(target = "artistName", source = "song.artist.username")
    @Mapping(target = "durationSeconds", source = "song.durationSeconds")
    PlaylistSongResponse toResponse(PlaylistSong playlistSong);
}
