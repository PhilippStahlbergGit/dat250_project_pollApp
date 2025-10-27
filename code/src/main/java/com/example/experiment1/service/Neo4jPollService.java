package com.example.experiment1.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class Neo4jPollService {

    private final PollCacheRepository repo;

    public Neo4jPollService(PollCacheRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void recordVote(String pollId, String option) {
        PollAggregate aggregate = repo.findById(pollId).orElseGet(() -> {
            return new PollAggregate(pollId, new HashMap<>(), LocalDateTime.now());
        });
        aggregate.getResults().merge(option, 1, Integer::sum);
        aggregate.setLastUpdated(LocalDateTime.now());
        repo.save(aggregate);
    }

    public Map<String, Integer> getAggregatedResults(String pollId) {
        return repo.findById(pollId)
                .map(PollAggregate::getResults)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found: " + pollId));
    }

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

}
