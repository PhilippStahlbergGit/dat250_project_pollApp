package com.example.experiment1.rabbitmq;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

public class Recv {

    private final static String EXCHANGE_NAME = "poll_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //Change Declare for queue or exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", false, false, null);
        
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "poll.*.created");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println(" [x] Received '" + message + "'");
            System.out.println("    From topic: " + routingKey);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        Thread.currentThread().join();
    }
}