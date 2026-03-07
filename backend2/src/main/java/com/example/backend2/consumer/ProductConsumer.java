package com.example.backend2.consumer;

import java.util.logging.Logger;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.backend2.model.CvCompareMessage;
import com.example.backend2.service.MessageService;
import com.rabbitmq.client.impl.AMQImpl.Channel.Open;

@Component
public class ProductConsumer {

    private final MessageService messageService;

    private final ChatClient chatClient;

    Logger logger = Logger.getLogger(ProductConsumer.class.getName());

    public ProductConsumer(MessageService messageService, ChatClient chatClient) {
        this.messageService = messageService;
        this.chatClient = chatClient;
    }

    @RabbitListener(queues = "queue-name")
    public void receiveMessage(CvCompareMessage message) {
        logger.info("Received message: " + message.getCvContent());
        try {
            Thread.sleep(3000l);

        String response = chatClient.prompt("Tell me a joke").call().content();							
        System.out.println(response);

            // messageService.sendStatusMessage(message.getId(), "Processed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
