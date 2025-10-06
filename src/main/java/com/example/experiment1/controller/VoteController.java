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
import com.example.experiment1.service.RedisPollService;
import com.example.experiment1.domain.Poll;
import com.example.experiment1.domain.Vote;
import com.example.experiment1.domain.VoteOption;
@CrossOrigin
@RestController
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    private PollManager pollManager;
    @Autowired
    private RedisPollService redisPollService;
    @Autowired
    RabbitMQPollService rabbitMQPollService;
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

        if (redisPollService.isRedisAvailable()) {
            Poll poll = pollManager.getPolls().get(pollId);
            if (poll != null && poll.getOptions() != null) {
                int optionIndex = vote.getOptionIndex() - 1;
                if (optionIndex >= 0 && optionIndex < poll.getOptions().size()) {
                    VoteOption option = poll.getOptions().get(optionIndex);
                    redisPollService.incrementVoteCount(pollId, option.getCaption());
                }
            }
        }
        rabbitMQPollService.publishVoteCreated(vote.getPollId(), vote.getOptionIndex()-1, userId);


    }

    @GetMapping
    public Collection<Vote> getAllVotes(){
        return pollManager.getVote().values();
    }

    


}
