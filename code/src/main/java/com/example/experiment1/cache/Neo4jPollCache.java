package com.example.experiment1.cache;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.experiment1.domain.OptionVote;
import com.example.experiment1.domain.PollAggregate;
import com.example.experiment1.repository.PollCacheRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import jakarta.annotation.PostConstruct;

// terminal command to run the docker container on linux (if other OS remove "sudo")
// sudo docker run \
//   --name neo4j-db \
//   --publish=7474:7474 --publish=7687:7687 \
//   --volume=$HOME/neo4j/data:/data \
//   --env NEO4J_AUTH=neo4j/supersecret \
//   neo4j:latest

@Service
public class Neo4jPollCache implements PollCache {

    private static final String EXCHANGE_NAME = "poll_exchange";
    private final PollCacheRepository repo;
    private final ConnectionFactory rabbitFactory = new ConnectionFactory();
    private Connection rabbitConnection; 
    private Channel rabbitChannel;

    public Neo4jPollCache(PollCacheRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Override
    public void updateVote(Long pollId, String option, int count, Map<Long,PollAggregate> pollAggregates) {
        // if poll is not saved in cache
        PollAggregate aggregate = repo.findById(pollId).orElseGet(() -> {
            // fill up results list from previous results stored in database

            PollAggregate existingAggregate = pollAggregates.get(pollId);
            if (existingAggregate != null) {
                return existingAggregate;
            }
            return new PollAggregate(pollId, new ArrayList<>(), LocalDateTime.now());
        });
    
        List<OptionVote> results = aggregate.getResults();
        boolean found = false;
        for (OptionVote ov : results) {
            if (ov.getOption().equals(option)) {
                int newCount = Math.max(0, ov.getVotes() + count);
                ov.setVotes(newCount);
                found = true;
                break;
            }
        }
        // if option is not saved in cache, i.e. no votes are registered for this option yet
        if (!found) {
            results.add(new OptionVote(option, Math.max(0, count)));
        }
        aggregate.setLastUpdated(LocalDateTime.now());
        repo.save(aggregate);
    }

    @Override
    public List<OptionVote> getAggregatedResults(Long pollId) {
        return repo.findById(pollId)
                .map(PollAggregate::getResults)
                .orElse(null);
    }

    @Override
    @Scheduled(fixedRate = 60000) // check every minute
    // @Scheduled (fixedRate = 10000) // for testing, check every 10 seconds
    public void cleanStaleAggregates() {
        System.out.println("Checking stale aggregates....");
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(10);
        // LocalDateTime cutoff = LocalDateTime.now(); // for testing
        repo.findAll().forEach(aggregate -> {
            if (aggregate.getLastUpdated().isBefore(cutoff)) {
                repo.deleteAggregateWithOptions(aggregate.getPollId());
                System.out.println("Deleted stale aggregate and its options: " + aggregate.getPollId());
            }
        });
    }


    @Override
    public void savePoll(PollAggregate pollAggregate) {
        repo.save(pollAggregate);
    }

    @Override
    public void removePoll(Long pollId) {
        repo.deleteById(pollId);
    }

    @Override
    public PollAggregate getPoll(Long pollId) {
        return repo.findById(pollId).orElse(null);
    }

    @PostConstruct
    public void startSubscriber() {
        try {
            rabbitFactory.setHost("localhost");
            rabbitConnection = rabbitFactory.newConnection();
            rabbitChannel = rabbitConnection.createChannel();
            rabbitChannel.exchangeDeclare(EXCHANGE_NAME, "topic");

            String queueName = rabbitChannel.queueDeclare().getQueue();
            rabbitChannel.queueBind(queueName, EXCHANGE_NAME, "poll.*.vote");

            DeliverCallback deliver = (consumerTag, delivery) -> {
                String body = new String(delivery.getBody(), StandardCharsets.UTF_8); 
        }; 
        rabbitChannel.basicConsume(queueName, true, deliver, consumerTag -> { });
    } catch (Exception e) {System.err.println("Error");}
    }

}
