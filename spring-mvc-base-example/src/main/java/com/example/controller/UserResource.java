package com.example.controller;

import com.example.persist.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable long userId) {
        return userService.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.save(user);
    }
}
