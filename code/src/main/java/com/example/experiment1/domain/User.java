package com.example.experiment1.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id 
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String username;
    private String email;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "createdByUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Poll> created = new LinkedHashSet<>();
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
        Vote vote = new Vote();
        vote.setOptionIndex(option.getPresentationOrder());
        vote.setVotesOn(option);
        return vote;
    }
}
