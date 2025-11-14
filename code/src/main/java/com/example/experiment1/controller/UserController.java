package com.example.experiment1.controller;

import com.example.experiment1.domain.jpa.User;
import com.example.experiment1.repository.jpa.UserRepository;

import org.springframework.security.core.Authentication;
import java.util.Collection;

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
    private UserRepository userRepository;

    @Autowired
    private PollManager pollManager;


    // TODO: Change this function to be inside the PollManager.class later
    @GetMapping("/me")
    public User getLoggedInUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
	return pollManager.meUser(authentication);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return pollManager.createUser(user);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return pollManager.getUsers().values();
    }

}
