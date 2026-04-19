package com.project.foodapp.controller;

import com.project.foodapp.dto.LoginRequest;
import com.project.foodapp.dto.UserDTO;
import com.project.foodapp.entity.User;
import com.project.foodapp.response.ApiResponse;
import com.project.foodapp.service.UserService;
import com.project.foodapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@Valid @RequestBody User user) {
        return new ApiResponse<>(
                true,
                "User created successfully",
                userService.saveUser(user)
        );
    }
    @GetMapping
    public ApiResponse<List<UserDTO>> getUsers() {
        return new ApiResponse<>(
                true,
                "Users fetched successfully",
                userService.getAllUsers()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        return new ApiResponse<>(
                true,
                "User fetched successfully",
                userService.getUserById(id)
        );
    }
    //to perform update
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {

        return new ApiResponse<>(
                true,
                "User updated successfully",
                userService.updateUser(id, user)
        );
    }

    //to perform delete
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        return new ApiResponse<>(
                true,
                "User deleted successfully",
                userService.deleteUser(id)
        );
    }


    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ApiResponse<String> loginUser(@RequestBody LoginRequest request) {

        User user = userService.loginUser(request);

        String token = jwtUtil.generateToken(user.getEmail() , user.getRole());

        return new ApiResponse<>(
                true,
                "Login successful",
                token
        );
    }
}