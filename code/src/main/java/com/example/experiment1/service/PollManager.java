package com.example.experiment1.service;

import com.example.experiment1.domain.User;
import com.example.experiment1.cache.Neo4jPollCache;
import com.example.experiment1.domain.Poll;
import com.example.experiment1.domain.Vote;
import com.example.experiment1.domain.VoteOption;
import com.example.experiment1.cache.PollCache;
import com.example.experiment1.repository.PollCacheRepository;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;

@Component
public class PollManager {

    private static int userIdCounter = 1;
    private static int pollIdCounter = 1;

    private final PollCache pollcache;

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

    public PollManager(PollCacheRepository repo) {
        this.pollcache = new Neo4jPollCache(repo);
    }

    public void createPoll(Poll poll, String userId) {
        poll.setPollId(String.valueOf(pollIdCounter++));
        poll.setPublishedAt(Instant.now());
        poll.setCreatedBy(userId);
        if (poll.getValidUntil() == null) {
            poll.setValidUntil(Instant.now().plusSeconds(86400));
        }
        this.getPolls().put(poll.getPollId(), poll);

        // if (redisPollService.isRedisAvailable()) {
        // redisPollService.storePollMetadata(poll.getPollId(), poll.getQuestion());
        // if (poll.getOptions() != null) {
        // for (VoteOption option : poll.getOptions()) {
        // redisPollService.setVoteCount(poll.getPollId(), option.getCaption(), 0);
        // }
        // }
        // }

    }

    public Collection<Poll> getAllPolls() {
        for (Poll poll : this.getPolls().values()) {
            // Reset votes for each option
            for (VoteOption option : poll.getOptions()) {
                option.setVotes(0);
            }
            // Count votes for each option
            this.getVote().values().stream()
                    .filter(v -> v.getPollId().equals(poll.getPollId()))
                    .forEach(v -> {
                        int idx = v.getOptionIndex() - 1; // 1-based index
                        if (idx >= 0 && idx < poll.getOptions().size()) {
                            poll.getOptions().get(idx).setVotes(
                                    poll.getOptions().get(idx).getVotes() + 1);
                        }
                    });
        }
        return this.getPolls().values();
    }

    public void deletePoll(String pollId) {
        this.getPolls().remove(pollId);
        // Remove all votes on this poll
        this.getVote().entrySet().removeIf(entry -> entry.getKey().endsWith(":" + pollId));
    }

    public void createVote(Vote vote, String userId, String pollId) {
        vote.setUserId(userId);
        vote.setPollId(pollId);
        vote.setPublishedAt(Instant.now());
        // Combine userId and pollId in a key
        String key = vote.getUserId() + ":" + vote.getPollId();

        // Remove old vote if it exists
        this.getVote().remove(key);

        // Add the new vote
        this.getVote().put(key, vote);

        String optionCaption = null;
        int optionIndexZeroBased = vote.getOptionIndex() - 1;
        Poll poll = this.getPolls().get(pollId);
        if (poll != null && poll.getOptions() != null && optionIndexZeroBased >= 0
                && optionIndexZeroBased < poll.getOptions().size()) {
            VoteOption option = poll.getOptions().get(optionIndexZeroBased);
            if (option != null)
                optionCaption = option.getCaption();
        }

    }

    public User createUser(User user) {
        user.setUserId(String.valueOf(userIdCounter++));
        this.getUsers().put(user.getUserId(), user);
        return user;

    }
}
