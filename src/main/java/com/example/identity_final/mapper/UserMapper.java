package com.example.identity_final.mapper;

import com.example.identity_final.Entity.User;
import com.example.identity_final.dto.request.UserCreationRequest;
import com.example.identity_final.dto.request.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    //map userupdaterequest vao user
    void updateuser(@MappingTarget User user, UserUpdateRequest request);
}
