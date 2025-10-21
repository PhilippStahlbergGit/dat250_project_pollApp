package com.example.experiment1.service;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@Service
public class RabbitMQPollService {
    private static final String EXCHANGE_NAME = "poll_exchange";
    private ConnectionFactory factory;
    private Channel channel;
    private Connection connection;

    public RabbitMQPollService() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        try  {connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            System.out.println("[x] Connected to RabbitMQ");
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to connect to RabbitMQ");
        }
    }
    

    public void publishPollCreated(String pollId, String question, String createdBy) {
        try {
            String routingKey = "poll." + pollId + ".created";
            String message = String.format("{\"pollId\":\"%s\",\"question\":\"%s\",\"createdBy\":\"%s\",\"event\":\"created\"}",
                    pollId, question, createdBy);

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sent poll created");
        } catch (Exception e) {
            System.err.println("Failed to publish poll created:" + e.getMessage());
        }
    }
    public void publishVoteCreated(String pollId, int optionIndex, String optionCaption, String createdBy) {
        try {
            String routingkey = "poll." + pollId + ".vote";
            String message = String.format(
                    "{\"pollId\":\"%s\",\"optionIndex\":%d,\"optionCaption\":\"%s\",\"createdBy\":\"%s\",\"event\":\"vote\"}",
                    pollId, optionIndex, optionCaption, createdBy);
            channel.basicPublish(EXCHANGE_NAME, routingkey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sent poll voted");

        } catch (Exception e) {
            System.err.println("Failed to publish poll voted:" + e.getMessage());

        }

    }

}
