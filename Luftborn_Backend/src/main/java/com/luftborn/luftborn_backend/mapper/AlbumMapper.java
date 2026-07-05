package com.luftborn.luftborn_backend.mapper;

import com.luftborn.luftborn_backend.dto.request.AlbumRequest;
import com.luftborn.luftborn_backend.dto.response.AlbumResponse;
import com.luftborn.luftborn_backend.model.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    Album toEntity(AlbumRequest request);

    @Mapping(target = "artistId", source = "artist.id")
    @Mapping(target = "artistName", source = "artist.username")
    AlbumResponse toResponse(Album album);
}
