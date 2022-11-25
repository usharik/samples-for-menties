package com.example.controller;

import com.example.persist.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable long userId) {
        return userService.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse(ex.getLocalizedMessage());
    }
}
