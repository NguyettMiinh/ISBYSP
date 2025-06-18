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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    //trc luc goi ham co role la admin ms goi ham dc
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("In method get user");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(Long id) {
        log.info("In method get user by id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found")));
    }
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext(); //get dc user hien tai
        String name = context.getAuthentication().getName(); //get user dang nhap hien tai
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
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
