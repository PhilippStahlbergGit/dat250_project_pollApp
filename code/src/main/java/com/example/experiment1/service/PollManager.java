package com.example.experiment1.service;

import com.example.experiment1.domain.User;
import com.example.experiment1.domain.Poll;
import com.example.experiment1.domain.Vote;
import com.example.experiment1.domain.VoteOption;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;

@Component
public class PollManager {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Poll> polls = new HashMap<>();
    private Map<String, Vote> votes = new HashMap<>();

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Poll> getPolls() {
        return polls;
    }

    public Map<String, Vote> getVote() {
        return votes;
    }

    public void createPoll(Poll poll, String userId) {
        poll.setPollId(String.valueOf(pollIdCounter++));
        poll.setPublishedAt(Instant.now());
        poll.setCreatedBy(userId);
        if (poll.getValidUntil() == null) {
            poll.setValidUntil(Instant.now().plusSeconds(86400));
        }
        pollManager.getPolls().put(poll.getPollId(), poll);

        if (redisPollService.isRedisAvailable()) {
            redisPollService.storePollMetadata(poll.getPollId(), poll.getQuestion());
            if (poll.getOptions() != null) {
                for (VoteOption option : poll.getOptions()) {
                    redisPollService.setVoteCount(poll.getPollId(), option.getCaption(), 0);
                }
            }
        }

        rabbitMQPollService.publishPollCreated(poll.getPollId(), poll.getQuestion(), userId);
    }

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
                                    poll.getOptions().get(idx).getVotes() + 1);
                        }
                    });
        }
        return pollManager.getPolls().values();
    }

    public void deletePoll(String pollId) {
        pollManager.getPolls().remove(pollId);
        // Remove all votes on this poll
        pollManager.getVote().entrySet().removeIf(entry -> entry.getKey().endsWith(":" + pollId));
    }
}
