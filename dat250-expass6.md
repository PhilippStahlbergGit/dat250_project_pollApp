# Report for Assignment 6

I managed to get the polls sent through the broker when creating a poll, which worked well. Using the RabbitMQPollService and using that in PollController. I used Recv.java to receive the messages, next step would be to implement it so that when a message is sent it updates the database.