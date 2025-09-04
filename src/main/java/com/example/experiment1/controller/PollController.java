package com.example.experiment1.controller;

import java.time.Instant;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.experiment1.service.PollManager;
import com.example.experiment1.domain.Poll;
import com.example.experiment1.domain.VoteOption;

@CrossOrigin
@RestController
@RequestMapping("/polls")
public class PollController {
    @Autowired PollManager pollManager;

    private static int pollIdCounter = 1;

    @PostMapping("{userId}")
    public void createPoll( @RequestBody Poll poll, @PathVariable String userId) {

        poll.setPollId(String.valueOf(pollIdCounter++));
        poll.setPublishedAt(Instant.now());
        poll.setCreatedBy(userId);
        if (poll.getValidUntil() == null) {
            poll.setValidUntil(Instant.now().plusSeconds(86400));
        }
        pollManager.getPolls().put(poll.getPollId(), poll);
    }

    @GetMapping
    public Collection<Poll> getAllPolls() {
            for (Poll poll : pollManager.getPolls().values()) {
        // Reset votes for each option
        for (VoteOption option : poll.getOptions()) {
            option.setVotes(0);
        }
        // Count votes for each option
        pollManager.getVote().values().stream()
            .filter(v -> v.getPollId().equals(poll.getPollId()))
            .forEach(v -> {
                int idx = v.getOptionIndex() - 1; // 1-based index
                if (idx >= 0 && idx < poll.getOptions().size()) {
                    poll.getOptions().get(idx).setVotes(
                        poll.getOptions().get(idx).getVotes() + 1
                    );
                }
            });
        }
        return pollManager.getPolls().values();
    }

    @DeleteMapping("delete/{pollId}")
    public void deletePoll(@PathVariable String pollId) {
        pollManager.getPolls().remove(pollId);
        //Remove all votes on this poll
        pollManager.getVote().entrySet().removeIf(entry -> entry.getKey().endsWith(":"+ pollId));
    }
}
