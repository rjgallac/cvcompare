package com.example.backend1.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue cvQueue() {
        return new Queue(QueueVars.CV_QUEUE, false);
    }

    @Bean
    public Queue cvResponseQueue() {
        return new Queue(QueueVars.CV_RESPONSE_QUEUE, false);
    }

    @Bean
    public Queue jobSpecQueue() {
        return new Queue(QueueVars.JOBSPEC_QUEUE, false);
    }

    @Bean
    public Queue jobSpecResponseQueue() {
        return new Queue(QueueVars.JOBSPEC_RESPONSE_QUEUE, false);
    }

    @Bean
    public Queue cvCompareQueue() {
        return new Queue(QueueVars.CV_COMPARE_QUEUE, false);
    }

    @Bean
    public Queue cvCompareResponseQueue() {
        return new Queue(QueueVars.CV_COMPARE_RESPONSE_QUEUE, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("exchange-name");
    }

    @Bean
    public Binding cvQueueBinding(Queue cvQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cvQueue).to(exchange).with("cv-routing-key");
        
    }

    @Bean
    public Binding cvResponseQueueBinding(Queue cvResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cvResponseQueue).to(exchange).with("cv-response-routing-key");
    }

    @Bean
    public Binding jobSpecQueueBinding(Queue jobSpecQueue, DirectExchange exchange) {
        return BindingBuilder.bind(jobSpecQueue).to(exchange).with("jobspec-routing-key");
    }

    @Bean
    public Binding jobspecResponseBinding(Queue jobSpecResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(jobSpecResponseQueue).to(exchange).with("jobspec-response-routing-key");
    }

    
    @Bean
    public Binding cvCompareQueueBinding(Queue cvCompareQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cvCompareQueue).to(exchange).with("jobspec-routing-key");
    }

    @Bean
    public Binding cvCompareResponseBinding(Queue cvCompareResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cvCompareResponseQueue).to(exchange).with("jobspec-response-routing-key");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}   