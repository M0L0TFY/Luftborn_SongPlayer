package com.luftborn.luftborn_backend.mapper;

import com.luftborn.luftborn_backend.dto.request.RegisterRequest;
import com.luftborn.luftborn_backend.dto.response.UserResponse;
import com.luftborn.luftborn_backend.model.User;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashedPassword", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);

    @Mapping(target = "createdAt", ignore = true)
    UserResponse toResponseFromDetails(UserDetailsImpl userDetails);
}
