package com.example.experiment1.service;
import com.example.experiment1.domain.User;
import com.example.experiment1.domain.Poll;
import com.example.experiment1.domain.Vote;
import org.springframework.stereotype.Component;
import java.util.Map;
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
}
