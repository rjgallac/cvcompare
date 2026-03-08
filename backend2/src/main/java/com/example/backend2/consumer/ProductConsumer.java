package com.example.backend2.consumer;

import java.util.logging.Logger;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.backend2.model.AiResponse;
import com.example.backend2.model.CvCompareMessage;
import com.example.backend2.model.CvCompareResponseMessage;
import com.example.backend2.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.impl.AMQImpl.Channel.Open;

@Component
public class ProductConsumer {

    private final MessageService messageService;

    private final ChatClient chatClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    Logger logger = Logger.getLogger(ProductConsumer.class.getName());

    public ProductConsumer(MessageService messageService, ChatClient chatClient) {
        this.messageService = messageService;
        this.chatClient = chatClient;
    }

    @RabbitListener(queues = "queue-name")
    public void receiveMessage(CvCompareMessage message) {
        // logger.info("Received message: " + message.getCvContent());
        StringBuilder sb = new StringBuilder();
        sb.append("can you extract the salary , position, company and location from this text please");
        sb.append("Job Description: " + message.getJobSpecContent());
      
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Compare the following CV content with the job description and provide a score from 0 to 100, where 100 means a perfect match.");
        sb2.append("Job Description: " + message.getJobSpecContent());
        sb2.append("CV Content: " + message.getCvContent());

OpenAiChatOptions options = OpenAiChatOptions.builder()
            .responseFormat(
                ResponseFormat.builder()
                .type(ResponseFormat.Type.JSON_SCHEMA)
                .jsonSchema("{\"type\":\"object\",\"properties\":{\"company\":{\"type\":\"string\"},\"salary\":{\"type\":\"string\"},\"title\":{\"type\":\"string\"},\"location\":{\"type\":\"string\"}},\"required\":[\"company\",\"salary\",\"title\",\"location\"]}")
                .build())
            .temperature(0.0)    
            .maxTokens(5000)
            .frequencyPenalty(0.0)
            .build();

        OpenAiChatOptions options2 = OpenAiChatOptions.builder()
            .responseFormat(
                ResponseFormat.builder()
                .type(ResponseFormat.Type.JSON_SCHEMA)
                .jsonSchema("{\"type\":\"object\",\"properties\":{\"score\":{\"type\":\"number\"}}}}")
                .build())
                
            .build();

        AiResponse aiResponse = chatClient
            .prompt(sb.toString())
            .options(options)
            .call()
            .entity(AiResponse.class);

         AiResponse aiResponse2 = chatClient
            .prompt(sb2.toString())
            .options(options2)
            .call()
            .entity(AiResponse.class);

        
        logger.info("Extracted AI Response: " + aiResponse2.getScore() + ", " + aiResponse.getCompany() + ", " + aiResponse.getSalary() + ", " + aiResponse.getTitle() + ", " + aiResponse.getLocation());
        logger.info("================================");

      
        CvCompareResponseMessage responseMessage = new CvCompareResponseMessage(
            message.getJobSpecId(),
            aiResponse2.getScore(),
            aiResponse.getCompany(),
            aiResponse.getSalary(),
            aiResponse.getTitle(),
            aiResponse.getLocation()
        );

        messageService.sendStatusMessage(responseMessage);
        
    }

}
