package com.example.identity_final.mapper;

import com.example.identity_final.Entity.User;
import com.example.identity_final.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
}
