package com.example.identity_final.Service;

import com.example.identity_final.Entity.User;
import com.example.identity_final.Repository.UserRepository;
import com.example.identity_final.dto.request.UserCreationRequest;
import com.example.identity_final.dto.request.UserUpdateRequest;
import com.example.identity_final.dto.response.UserResponse;
import com.example.identity_final.enums.Role;
import com.example.identity_final.exception.AppException;
import com.example.identity_final.exception.ErrorCode;
import com.example.identity_final.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            //RuntimeException no chi nhan String
            throw new AppException(ErrorCode.USER_EXIST);
        }
        //map du lieu
        User user = userMapper.toUser(request);
        //ma hoa va giai ma duoi 1s

        //ma hoa
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name()); // // trả về "USER"
        //tao role cho acc
        user.setRoles(roles);
        //luu vao trong db
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers(){

        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
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
