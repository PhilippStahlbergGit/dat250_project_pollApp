package com.example.experiment1.cache;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.experiment1.domain.OptionVote;
import com.example.experiment1.domain.PollAggregate;
import com.example.experiment1.repository.PollCacheRepository;

// terminal command to run the docker container on linux (if other OS remove "sudo")
// sudo docker run \
//   --name neo4j-db \
//   --publish=7474:7474 --publish=7687:7687 \
//   --volume=$HOME/neo4j/data:/data \
//   --env NEO4J_AUTH=neo4j/supersecret \
//   neo4j:latest

@Service
public class Neo4jPollCache implements PollCache {

    private final PollCacheRepository repo;

    public Neo4jPollCache(PollCacheRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Override
    public void updateVote(Long pollId, String option, int count) {
        PollAggregate aggregate = repo.findById(pollId).orElseGet(() -> {
            return new PollAggregate(pollId, new ArrayList<>(), LocalDateTime.now());
        });
        aggregate.getResults().add(new OptionVote(option, count));
        aggregate.setLastUpdated(LocalDateTime.now());
        repo.save(aggregate);
    }

    @Override
    public List<OptionVote> getAggregatedResults(Long pollId) {
        return repo.findById(pollId)
                .map(PollAggregate::getResults)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found: " + pollId));
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void cleanStaleAggregates() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(10);
        repo.findAll().forEach(aggregate -> {
            if (aggregate.getLastUpdated().isBefore(cutoff)) {
                repo.delete(aggregate);
            }
        });
    }

    public static void main(String... args) {
        final String dbUri = ("bolt://localhost:7687");
        final String dbUser = ("neo4j");
        final String dbPassword = ("supersecret");

        try (var driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword))) {
            driver.verifyConnectivity();
            System.out.println("Connection established");
            driver.close();
        }
    }

    @Override
    public void savePoll(PollAggregate pollAggregate) {
        repo.save(pollAggregate);
    }

    @Override
    public void removePoll(Long pollId) {
        repo.deleteById(pollId);
    }

}
