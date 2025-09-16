package com.example.experiment1.domain;

import java.util.LinkedHashSet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String userId;
    private String username;
    private String email;
    private LinkedHashSet<Poll> created;
        /**
     * Creates a new User object with given username and email.
     * The id of a new user object gets determined by the database.
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.created = new LinkedHashSet<>();
    }

    /**
     * Creates a new Poll object for this user
     * with the given poll question
     * and returns it.
     */
    public Poll createPoll(String question) {
        Poll poll = new Poll();
        poll.setQuestion(question);
        poll.setCreatedByUser(this);
        this.created.add(poll);
        return poll;
    }

    /**
     * Creates a new Vote for a given VoteOption in a Poll
     * and returns the Vote as an object.
     */
    public Vote voteFor(VoteOption option) {
        // TODO: implement
        Vote vote = new Vote();
        vote.setOptionIndex(option.getPresentationOrder());
        return vote;
    }
}
