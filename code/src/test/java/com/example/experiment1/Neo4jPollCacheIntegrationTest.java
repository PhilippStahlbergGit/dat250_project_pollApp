package com.example.experiment1;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.experiment1.cache.PollCache;
import com.example.experiment1.domain.neo4j.OptionVote;
import com.example.experiment1.domain.neo4j.PollAggregate;

@SpringBootTest
public class Neo4jPollCacheIntegrationTest {

    @Autowired
    private PollCache pollCache;

    // currently the options are returned in reverse order from how they are saved,
    // so tests are written accordingly

    @Test
    void testSaveAndRetrievePoll() {
        List<OptionVote> results = new ArrayList<>();
        results.add(new OptionVote("Red", 0));
        results.add(new OptionVote("Blue", 0));

        PollAggregate aggregate = new PollAggregate(1L, results, LocalDateTime.now());
        pollCache.savePoll(aggregate);

        List<OptionVote> fetched = pollCache.getAggregatedResults(1L);
        assertEquals(2, fetched.size());
        assertEquals("Red", fetched.get(1).getOption());
        assertEquals(0, fetched.get(1).getVotes());
        assertEquals("Blue", fetched.get(0).getOption());
        assertEquals(0, fetched.get(0).getVotes());
    }

    @Test
    void testUpdateVote() {
        List<OptionVote> results = new ArrayList<>();
        results.add(new OptionVote("Red", 0));
        results.add(new OptionVote("Blue", 0));

        PollAggregate aggregate = new PollAggregate(2L, results, LocalDateTime.now());
        pollCache.savePoll(aggregate);

        pollCache.updateVote(2L, "Red", 1, new HashMap<>());
        List<OptionVote> updated = pollCache.getAggregatedResults(2L);

        assertEquals(1, updated.get(1).getVotes());
        assertEquals(0, updated.get(0).getVotes());
    }

    @Test
    void testRemovePoll() {
        List<OptionVote> results = new ArrayList<>();
        results.add(new OptionVote("Green", 3));
        pollCache.savePoll(new PollAggregate(3L, results, LocalDateTime.now()));

        pollCache.removePoll(3L);

        assertNull(pollCache.getAggregatedResults(3L));
    }
}
