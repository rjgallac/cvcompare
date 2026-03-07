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
        sb.append("You are a powerful cv analysis service for comparing CVs and provide your responses in JSON format for a calling server.Only return the final answer. Do not include any reasoning, thoughts, or explanations.");
        sb.append("Compare the following CV content with the job description and provide a score from 0 to 100, where 100 means a perfect match.");
        sb.append("CV Content: " + message.getCvContent());
        sb.append("Job Description: " + message.getJobSpecContent());
        sb.append("Can you extract score, the company name, salary, title and location and provide them in a JSON format like this:");
        
        sb.append("{\"score\": 85, \"company\": \"Tech Company\", \"salary\": \"100k-120k\", \"title\": \"Software Engineer\", \"location\": \"San Francisco\"}");
        sb.append("Only provide the JSON response without any additional text.If you cannot extract any of the fields, please set them to null or 0 for score.");
        sb.append("if you respond with anything other than json the program will break. Only JSON should be returned nothing else, no other text should be returned.");
        sb.append("If you cannot extract any of the fields, please set them to null or 0 for score.");
        // OpenAiChatOptions options = OpenAiChatOptions.builder()
        //         .model("qwen3.5-9b")
        //         // .outputSchema("AiResponse")
        //         .temperature(0.2)
        //         .maxTokens(500)
                
        //         .build();
        // AiResponse aiResponse = chatClient
        //     .prompt(sb.toString())
        //     .options(options)
        //     .call()
        //     .entity(AiResponse.class);	
OpenAiChatOptions options = OpenAiChatOptions.builder()
        .responseFormat(
            ResponseFormat.builder()
            .type(ResponseFormat.Type.JSON_SCHEMA)
            .jsonSchema("{\"type\":\"object\",\"properties\":{\"score\":{\"type\":\"number\"},\"company\":{\"type\":\"string\"},\"salary\":{\"type\":\"string\"},\"title\":{\"type\":\"string\"},\"location\":{\"type\":\"string\"}}}")
            .build())
            
        .build();

        AiResponse aiResponse = chatClient
            .prompt(sb.toString())
            .options(options)
            .call()
            .entity(AiResponse.class);

    //    String response = chatClient
    //         .prompt(sb.toString())
    //         .call()
    //         .content();
        
        logger.info("Extracted AI Response: " + aiResponse.getScore() + ", " + aiResponse.getCompany() + ", " + aiResponse.getSalary() + ", " + aiResponse.getTitle() + ", " + aiResponse.getLocation());
        logger.info("================================");

      
        CvCompareResponseMessage responseMessage = new CvCompareResponseMessage(
            message.getJobSpecId(),
            aiResponse.getScore(),
            aiResponse.getCompany(),
            aiResponse.getSalary(),
            aiResponse.getTitle(),
            aiResponse.getLocation()
        );

        messageService.sendStatusMessage(responseMessage);
        
    }

}
