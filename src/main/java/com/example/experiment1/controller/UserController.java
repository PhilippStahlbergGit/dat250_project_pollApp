package com.example.experiment1.controller;
import com.example.experiment1.domain.User;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.experiment1.service.PollManager;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PollManager pollManager;

    @PostMapping
    public void createUser( @RequestBody User user ) {
        pollManager.getUsers().put(user.getUsername(), user);
    }
    @GetMapping
    public Collection<User> getAllUsers() {
        return pollManager.getUsers().values();
    }



}
