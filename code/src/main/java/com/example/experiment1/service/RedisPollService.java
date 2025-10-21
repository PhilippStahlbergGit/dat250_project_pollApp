package com.example.experiment1.service;


import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
public class RedisPollService {
    @Autowired StringRedisTemplate stringRedisTemplate;

    private static final long CACHE_TTL_MINUTES = 30;

    private static final String EXCHANGE_NAME = "poll_exchange";
    private final ConnectionFactory rabbitFactory = new ConnectionFactory();
    private Connection rabbitConnection;
    private Channel rabbitChannel;
    private final ObjectMapper mapper = new ObjectMapper();


    public void storePollMetadata(String pollId, String question) {
        String key = "poll:" + pollId;
        stringRedisTemplate.opsForHash().put(key, "id", pollId);
        stringRedisTemplate.opsForHash().put(key, "question", question);

        stringRedisTemplate.expire(key, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }


    public void setVoteCount(String pollId, String optionCaption, int voteCount) {
        String votesKey = "poll:" + pollId + ":votes";
        stringRedisTemplate.opsForHash().put(votesKey, optionCaption, String.valueOf(voteCount));
        stringRedisTemplate.expire(votesKey, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }
    public Long incrementVoteCount(String pollId, String optionCaption) {
        String votesKey = "poll:" + pollId + ":votes";
        Long newCount = stringRedisTemplate.opsForHash().increment(votesKey, optionCaption, 1);

        stringRedisTemplate.expire(votesKey, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return newCount;
    }
    public boolean isPollCached(String pollId) {
        String votesKey = "poll:" + pollId + ":votes";
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(votesKey));
    }
    

    public boolean isRedisAvailable() {
        try {
            stringRedisTemplate.getConnectionFactory().getConnection().ping();

            return true;
        } catch (Exception e) {
            return false;
        }
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
            try {
                JsonNode node = mapper.readTree(body);
                String pollId = node.path("pollId").asText(null);
                String optionCaption = node.path("optionCaption").asText(null);
                incrementVoteCount(pollId, optionCaption);

                
            } catch (Exception e) {
                System.err.println("Invalid message");
            }
        };
        rabbitChannel.basicConsume(queueName, true, deliver, consumerTag -> { });
    } catch (Exception e) {System.err.println("Error");}
    }
}
