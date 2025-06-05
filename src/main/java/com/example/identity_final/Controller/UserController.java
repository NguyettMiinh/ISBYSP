package com.example.identity_final.Controller;

import com.example.identity_final.Entity.User;
import com.example.identity_final.Service.UserService;
import com.example.identity_final.dto.request.UserCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public User createUser(@RequestBody UserCreationRequest request) {
        return userService.createUser(request);
    }
}

