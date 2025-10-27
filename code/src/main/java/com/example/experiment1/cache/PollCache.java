package com.example.experiment1.cache;

public interface PollCache {

    void recordVote(String pollId, String option);

    java.util.Map<String, Integer> getAggregatedResults(String pollId);

    void cleanStaleAggregates();
}
