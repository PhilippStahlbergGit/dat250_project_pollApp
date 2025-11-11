package com.example.experiment1.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Id;

@Node
public class PollAggregate {

    @Id
    private final Long pollId;
    @Relationship(type = "HAS_OPTION", direction = Relationship.Direction.OUTGOING)
    private final List<OptionVote> results;
    private LocalDateTime lastUpdated;

    public PollAggregate(Long pollId, List<OptionVote> results, LocalDateTime lastUpdated) {
        this.pollId = pollId;
        this.results = results;
        this.lastUpdated = lastUpdated;
    }

    public List<OptionVote> getResults() {
        return results;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
