package com.example.experiment1.service;

import com.example.experiment1.domain.User;
import com.example.experiment1.domain.OptionVote;
import com.example.experiment1.domain.Poll;
import com.example.experiment1.domain.PollAggregate;
import com.example.experiment1.domain.Vote;
import com.example.experiment1.domain.VoteOption;
import com.example.experiment1.cache.PollCache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class PollManager {

    private static int userIdCounter = 1;
    private static int pollIdCounter = 1;

    private final PollCache pollCache;

    private Map<String, User> users = new HashMap<>();
    private Map<Long, Poll> polls = new HashMap<>();
    private Map<String, Vote> votes = new HashMap<>();

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<Long, Poll> getPolls() {
        return polls;
    }

    public Map<String, Vote> getVotes() {
        return votes;
    }

    public PollManager(PollCache pollCache) {
        this.pollCache = pollCache;
    }

    public void createPoll(Poll poll, String userId) {
        poll.setPollId(String.valueOf(poll.getId()));
        poll.setPublishedAt(Instant.now());
        poll.setCreatedBy(userId);
        if (poll.getValidUntil() == null) {
            poll.setValidUntil(Instant.now().plusSeconds(86400));
        }
        this.getPolls().put(poll.getId(), poll);

        // Save in cache
        List<OptionVote> results = new ArrayList<>();
        for (VoteOption option : poll.getOptions()) {
            results.add(new OptionVote(option.getCaption(), 0));
        }
        pollCache.savePoll(new PollAggregate(poll.getId(), results, LocalDateTime.now()));
    }

    public Collection<Poll> getAllPolls() {
        for (Poll poll : this.getPolls().values()) {
            List<OptionVote> aggregatedResults = pollCache.getAggregatedResults(poll.getId());
            for (VoteOption option : poll.getOptions()) {
                option.setVotes(aggregatedResults.stream()
                        .filter(ov -> ov.getOption().equals(option.getCaption()))
                        .map(OptionVote::getVotes)
                        .findFirst()
                        .orElse(0));
            }
        }
        return this.getPolls().values();
    }

    public void deletePoll(Long pollId) {
        this.getPolls().remove(pollId);
        // Remove all votes on this poll
        this.getVotes().entrySet().removeIf(entry -> entry.getKey().endsWith(":" + pollId));
        pollCache.removePoll(pollId);
    }

    public void createVote(Vote vote, String userId, Long pollId) {
        vote.setUserId(userId);
        vote.setPollId(pollId.toString());
        vote.setPublishedAt(Instant.now());
        String key = vote.getUserId() + ":" + vote.getPollId();
        Vote oldVote = this.getVotes().put(key, vote);

        Poll poll = this.getPolls().get(pollId);
        if (poll != null && poll.getOptions() != null) {
            int newIdx = vote.getOptionIndex() - 1;
            String optionCaption = poll.getOptions().get(newIdx).getCaption();

            // Decrement old vote in cache
            if (oldVote != null) {
                int oldIdx = oldVote.getOptionIndex() - 1;
                String oldCaption = poll.getOptions().get(oldIdx).getCaption();
                pollCache.updateVote(pollId, oldCaption, -1);
            }

            // Increment new vote in cache
            pollCache.updateVote(pollId, optionCaption, 1);
        }
    }

    public User createUser(User user) {
        user.setUserId(String.valueOf(userIdCounter++));
        this.getUsers().put(user.getUserId(), user);
        return user;

    }
}
