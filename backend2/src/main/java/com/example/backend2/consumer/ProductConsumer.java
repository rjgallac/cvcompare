package com.example.backend2.consumer;

import java.util.logging.Logger;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.backend2.config.QueueVars;
import com.example.backend2.message.CvCompareMessage;
import com.example.backend2.message.CvCompareResponseMessage;
import com.example.backend2.message.CvMessage;
import com.example.backend2.message.CvResponseMessage;
import com.example.backend2.message.JobSpecMessage;
import com.example.backend2.message.JobSpecResponseMessage;
import com.example.backend2.model.AiCompareResponse;
import com.example.backend2.model.AiJobSpecResponse;
import com.example.backend2.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductConsumer {

    private final MessageService messageService;

    private final ChatClient chatClient;

    Logger logger = Logger.getLogger(ProductConsumer.class.getName());

    public ProductConsumer(MessageService messageService, ChatClient chatClient) {
        this.messageService = messageService;
        this.chatClient = chatClient;
    }

    @RabbitListener(queues = QueueVars.CV_QUEUE)
    public void receiveSuggestMessage(CvMessage cvMessage) {
        // logger.info("Received suggest message: " + cvSuggestMessage.getCvContent());
        // Process the message and generate suggestions
        StringBuilder suggestions = new StringBuilder();
        suggestions.append("Recommend improvements for the following CV please");
        suggestions.append("CV Content: " + cvMessage.getCvContent());
        String aiResponse = chatClient
            .prompt(suggestions.toString())
            .call().content();
        // logger.info("Generated suggestions: " + suggestions);
        // Send the suggestions back to the first service
        CvResponseMessage cvResponseMessage = new CvResponseMessage(cvMessage.getCvId(), aiResponse);
        messageService.sendSuggestMessage(cvResponseMessage);
    }

    @RabbitListener(queues = QueueVars.JOBSPEC_QUEUE)
    public void receiveMessage(JobSpecMessage jobSpecMessage) {
        // logger.info("Received message: " + message.getCvContent());
        StringBuilder sb = new StringBuilder();
        sb.append("can you extract the salary , position, company and location from this text please");
        sb.append("Job Description: " + jobSpecMessage.getJobSpecContent());

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

        AiJobSpecResponse aiJobSpecResponse = chatClient
            .prompt(sb.toString())
            .options(options)
            .call()
            .entity(AiJobSpecResponse.class);
        
        logger.info("Extracted AI Response: " + aiJobSpecResponse.getCompany() + ", " + aiJobSpecResponse.getSalary() + ", " + aiJobSpecResponse.getTitle() + ", " + aiJobSpecResponse.getLocation());
        logger.info("================================");
      
        JobSpecResponseMessage jobSpecResponseMessage = new JobSpecResponseMessage(
            jobSpecMessage.getJobSpecId(),
            aiJobSpecResponse.getCompany(),
            aiJobSpecResponse.getSalary(),
            aiJobSpecResponse.getTitle(),
            aiJobSpecResponse.getLocation()
        );

        messageService.sendStatusMessage(jobSpecResponseMessage);  
    }

    @RabbitListener(queues = QueueVars.CV_COMPARE_QUEUE)
    public void receiveCompareMessage(CvCompareMessage cvCompareMessage) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Compare the following CV content with the job description and provide a score from 0 to 100, where 100 means a perfect match.");
        sb2.append("Job Description: " + cvCompareMessage.getJobSpecContent());
        sb2.append("CV Content: " + cvCompareMessage.getCvContent());

        OpenAiChatOptions options2 = OpenAiChatOptions.builder()
            .responseFormat(
                ResponseFormat.builder()
                .type(ResponseFormat.Type.JSON_SCHEMA)
                .jsonSchema("{\"type\":\"object\",\"properties\":{\"comparison\":{\"type\":\"string\"},\"score\":{\"type\":\"number\"}}}}")
                .build())
            .build();

        AiCompareResponse aiCompareResponse = chatClient
            .prompt(sb2.toString())
            .options(options2)
            .call()
            .entity(AiCompareResponse.class);
            CvCompareResponseMessage cvCompareResponseMessage = new CvCompareResponseMessage(cvCompareMessage.getCvCompareId(), aiCompareResponse.getScore(), aiCompareResponse.getComparison());
        messageService.sendCompareMessage(cvCompareResponseMessage);
    }

}
