package com.example.identity_final.Service;

import com.example.identity_final.Entity.User;
import com.example.identity_final.Repository.UserRepository;
import com.example.identity_final.dto.request.UserCreationRequest;
import com.example.identity_final.dto.request.UserUpdateRequest;
import com.example.identity_final.dto.response.UserResponse;
import com.example.identity_final.exception.AppException;
import com.example.identity_final.exception.ErrorCode;
import com.example.identity_final.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            //RuntimeException no chi nhan String
            throw new AppException(ErrorCode.USER_EXIST);
        }
        //map du lieu
        User user = userMapper.toUser(request);

        //luu vao trong db
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUser(Long id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found")));
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("user not found"));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    // o tang service, khi tra ve data cho tang controller, khong tra ve "Entity", can tra ve dto.response
    //autowired ko phai la best pratice de inject bean vao class, ngta khuyen khich dungf contructor de inject bean
    //su dung lombok
}
