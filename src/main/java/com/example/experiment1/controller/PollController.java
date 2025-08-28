package com.example.experiment1.controller;

import java.time.Instant;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.experiment1.service.PollManager;
import com.example.experiment1.domain.Poll;

@RestController
@RequestMapping("/polls")
public class PollController {
    @Autowired PollManager pollManager;

    private static int pollIdCounter = 1;

    @PostMapping("user/{userId}")
    public void createPoll( @RequestBody Poll poll, @PathVariable String userId) {

        poll.setPollId(String.valueOf(pollIdCounter++));
        poll.setPublishedAt(Instant.now());
        poll.setCreatedBy(userId);
        if (poll.getValidUntil() == null) {
            poll.setValidUntil(Instant.now().plusSeconds(86400));
        }
        pollManager.getPolls().put(poll.getQuestion(), poll);
    }

    @GetMapping
    public Collection<Poll> getAllPolls() {
        return pollManager.getPolls().values();
    }
}
