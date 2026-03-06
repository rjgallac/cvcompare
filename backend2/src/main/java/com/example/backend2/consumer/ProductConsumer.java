package com.example.backend2.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductConsumer {

    @RabbitListener(queues = "queue-name")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

}
