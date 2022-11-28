package com.example.controller;

import com.example.persist.User;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user";
    }

    @GetMapping("/new")
    public String findById(Model model) {
        model.addAttribute("user", new User());
        return "user_form";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user_form";
    }

    @PostMapping()
    public String save(User user) {
        userService.save(user);
        return "redirect:/user";
    }

    @DeleteMapping("/{id}")
    public String findById(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/user";
    }
}
