package com.example.identity_final.mapper;

import com.example.identity_final.Entity.User;
import com.example.identity_final.dto.request.UserCreationRequest;
import com.example.identity_final.dto.request.UserUpdateRequest;
import com.example.identity_final.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    //map userupdaterequest vao user
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    // chuyen doi tuong user thanh userResponse
//    Khi bạn gọi toUserResponse(user), MapStruct sẽ tự động ánh xạ các field phù hợp,
//    và bỏ qua password, createdAt vì UserResponse không chứa chúng.
    UserResponse toUserResponse(User user);
}
