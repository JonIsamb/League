package com.jisamb.league.controller;


import com.jisamb.league.dto.request.CreateUserRequest;
import com.jisamb.league.dto.response.UserDTO;
import com.jisamb.league.entity.User;
import com.jisamb.league.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO insertUser(@Validated @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
}
