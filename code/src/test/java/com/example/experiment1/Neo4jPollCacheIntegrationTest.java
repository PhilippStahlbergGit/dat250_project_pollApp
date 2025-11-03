package com.example.experiment1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.experiment1.cache.PollCache;
import com.example.experiment1.domain.PollAggregate;

@SpringBootTest
public class Neo4jPollCacheIntegrationTest {

    @Autowired
    private PollCache pollCache;

    @Test
    void testSaveAndRetrievePoll() {
        Map<String, Integer> results = new HashMap<>();
        results.put("Red", 0);
        results.put("Blue", 0);

        PollAggregate aggregate = new PollAggregate(1L, results, LocalDateTime.now());
        pollCache.savePoll(aggregate);

        Map<String, Integer> fetched = pollCache.getAggregatedResults(1L);
        assertEquals(results.keySet(), fetched.keySet());
    }

    @Test
    void testUpdateVote() {
        Map<String, Integer> results = new HashMap<>();
        results.put("Red", 0);
        results.put("Blue", 0);

        PollAggregate aggregate = new PollAggregate(2L, results, LocalDateTime.now());
        pollCache.savePoll(aggregate);

        pollCache.updateVote(2L, "Red", 1);
        Map<String, Integer> updated = pollCache.getAggregatedResults(2L);

        assertEquals(Integer.valueOf(1), updated.get("Red"));
        assertEquals(Integer.valueOf(0), updated.get("Blue"));
    }

    @Test
    void testRemovePoll() {
        Map<String, Integer> results = Map.of("Green", 3);
        pollCache.savePoll(new PollAggregate(3L, results, LocalDateTime.now()));

        pollCache.removePoll(3L);

        assertNull(pollCache.getAggregatedResults(3L));
    }
}
