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

    public RabbitMQPollService() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    public void publishPollCreated(String pollId, String question, String createdBy) {
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", false, false, null);
            String routingKey = "poll." + pollId + ".created";
            String message = String.format("{\"pollId\":\"%s\",\"question\":\"%s\",\"createdBy\":\"%s\",\"event\":\"created\"}", 
                pollId, question, createdBy);

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sent poll created");
        } catch (Exception e) {
            System.err.println("Failed to publish poll created:" + e.getMessage());
        }
    }

}
