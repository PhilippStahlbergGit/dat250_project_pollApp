package com.example.experiment1.domain;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

import org.springframework.data.neo4j.core.schema.Id;

@Node
public class PollAggregate {

    @Id
    @GeneratedValue
    private final String pollId;
    private final Map<String, Integer> results;
    private LocalDateTime lastUpdated;

    public PollAggregate(String pollId, Map<String, Integer> results, LocalDateTime lastUpdated) {
        this.pollId = pollId;
        this.results = results;
        this.lastUpdated = lastUpdated;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
