package com.pinnacle.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinnacle.backend.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import com.pinnacle.backend.model.UserPayloadModel;


@RestController
@RequestMapping("/payload")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserPayloadModel> getAllUsers() {
        return userService.getAllUsers();
    }
}
