package com.example.experiment1.cache;

import java.util.List;
import java.util.Map;

import com.example.experiment1.domain.OptionVote;
import com.example.experiment1.domain.PollAggregate;

public interface PollCache {

    /**
     * Record a vote for a given poll and option.
     * 
     * @param pollId the poll ID
     * @param option the option being voted on
     * @param count  the number of votes (can be negative to decrement)
     */
    void updateVote(Long pollId, String option, int count);

    /**
     * Get aggregated results for a given poll.
     * 
     * @param pollId the poll ID
     * @return a map of option captions to their vote counts
     */
    List<OptionVote> getAggregatedResults(Long pollId);

    /**
     * Clean up stale poll aggregates that haven't been updated recently.
     */
    void cleanStaleAggregates();

    /**
     * Save a new poll aggregate to the cache.
     * 
     * @param pollAggregate the poll aggregate to save
     */
    void savePoll(PollAggregate pollAggregate);

    /**
     * Remove a poll aggregate from the cache.
     * 
     * @param pollId the poll ID to remove
     */
    void removePoll(Long pollId);
}
