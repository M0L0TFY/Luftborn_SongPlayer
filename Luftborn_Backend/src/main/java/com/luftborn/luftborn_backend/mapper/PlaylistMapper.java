package com.luftborn.luftborn_backend.mapper;

import com.luftborn.luftborn_backend.dto.request.PlaylistRequest;
import com.luftborn.luftborn_backend.dto.response.PlaylistResponse;
import com.luftborn.luftborn_backend.model.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalDurationSeconds", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Playlist toEntity(PlaylistRequest request);

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerUsername", source = "owner.username")
    @Mapping(target = "isPublic", source = "public") //Playlist.isPublic field turns into Playlist.Public in mapstruct
    PlaylistResponse toResponse(Playlist playlist);
}
