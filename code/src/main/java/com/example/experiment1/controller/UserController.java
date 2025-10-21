package com.example.experiment1.controller;
import com.example.experiment1.domain.Role;
import com.example.experiment1.domain.User;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.experiment1.service.PollManager;
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PollManager pollManager;

    private static int userIdCounter = 1;

    @PostMapping
    public User createUser( @RequestBody User user ) {
        user.setUserId(String.valueOf(userIdCounter++));
        pollManager.getUsers().put(user.getUserId(), user);
        if(user.getRoles() == null || user.getRoles().isEmpty()){
            user.setRoles(Set.of(Role.NORMAL));
        }
        return user;
    }
    @GetMapping
    public Collection<User> getAllUsers() {
        return pollManager.getUsers().values();
    }



}
