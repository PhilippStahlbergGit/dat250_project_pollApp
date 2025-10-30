package com.example.experiment1.controller;

import java.time.Instant;
import java.util.Collection;

import com.example.experiment1.service.RabbitMQPollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.experiment1.service.PollManager;
import com.example.experiment1.domain.Vote;

@CrossOrigin
@RestController
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    private PollManager pollManager;

    @PostMapping("/{userId}/{pollId}")
    public void createVote(@RequestBody Vote vote, @PathVariable String userId, @PathVariable Long pollId) {
        pollManager.createVote(vote, userId, pollId);

    }

    @GetMapping
    public Collection<Vote> getAllVotes() {
        return pollManager.getVotes().values();
    }

}
