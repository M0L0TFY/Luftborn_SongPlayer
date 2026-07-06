package com.luftborn.luftborn_backend.mapper;

import com.luftborn.luftborn_backend.dto.request.SongRequest;
import com.luftborn.luftborn_backend.dto.response.SongResponse;
import com.luftborn.luftborn_backend.model.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    @Mapping(target = "playlistSongs", ignore = true)
    Song toEntity(SongRequest request);

    @Mapping(target = "artistId", source = "artist.id")
    @Mapping(target = "artistName", source = "artist.username")
    @Mapping(target = "albumId", source = "album.id")
    @Mapping(target = "albumTitle", source = "album.title")
    SongResponse toResponse(Song song);
}
