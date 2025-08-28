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
import com.example.experiment1.domain.Vote;

@RestController
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    private PollManager pollManager;

    @PostMapping("/{userId}/{pollId}")
    public void createVote( @RequestBody Vote vote, @PathVariable String userId, @PathVariable String pollId) {
        vote.setUserId(userId);
        vote.setPollId(pollId);
        vote.setPublishedAt(Instant.now());
        //Combine userId and pollId in a key
        String key = vote.getUserId() + ":" + vote.getPollId();

        //Remove old vote if it exists
        pollManager.getVote().remove(key);
        
        //Add the new vote
        pollManager.getVote().put(key, vote);
    }

    @GetMapping
    public Collection<Vote> getAllVotes(){
        return pollManager.getVote().values();
    }

    


}
