package com.example.identity_final.Service;

import com.example.identity_final.Entity.User;
import com.example.identity_final.Repository.UserRepository;
import com.example.identity_final.dto.request.UserCreationRequest;
import com.example.identity_final.dto.request.UserUpdateRequest;
import com.example.identity_final.exception.AppException;
import com.example.identity_final.exception.ErrorCode;
import com.example.identity_final.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
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

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public User updateUser(Long id, UserUpdateRequest request) {
        User user = getUser(id);
        userMapper.updateuser(user, request);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
