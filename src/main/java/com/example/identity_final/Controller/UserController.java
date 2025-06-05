package com.example.identity_final.Controller;

import com.example.identity_final.Entity.User;
import com.example.identity_final.Service.UserService;
import com.example.identity_final.dto.request.UserCreationRequest;
import com.example.identity_final.dto.request.UserUpdateRequest;
import com.example.identity_final.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequest request){
        return userService.updateUser(id,request);
    }

    //neu mun tra ve data trong ham xoa thi cu tra thoi
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "user is deleted";
    }
}

