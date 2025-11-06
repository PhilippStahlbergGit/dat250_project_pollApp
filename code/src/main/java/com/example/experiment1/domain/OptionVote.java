package com.example.experiment1.domain;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class OptionVote {

    @Id
    @GeneratedValue
    private Long id;

    private final String option;
    private Integer votes;

    public OptionVote(String option, Integer votes) {
        this.option = option;
        this.votes = votes;
    }

    public Long getId() {
        return id;
    }

    public String getOption() {
        return option;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
