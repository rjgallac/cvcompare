package com.example.backend2.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend2.config.QueueVars;
import com.example.backend2.message.CvCompareResponseMessage;
import com.example.backend2.message.CvMessage;
import com.example.backend2.message.CvResponseMessage;
import com.example.backend2.message.JobSpecResponseMessage;


@Service
public class MessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendStatusMessage(JobSpecResponseMessage jobSpecResponseMessage) {
        rabbitTemplate.convertAndSend(QueueVars.JOBSPEC_RESPONSE_QUEUE, jobSpecResponseMessage);
    }

    public void sendSuggestMessage(CvResponseMessage cvResponseMessage) {
        rabbitTemplate.convertAndSend(QueueVars.CV_RESPONSE_QUEUE, cvResponseMessage);
    }

    public void sendCompareMessage(CvCompareResponseMessage cvCompareResponseMessage){
        rabbitTemplate.convertAndSend( QueueVars.CV_COMPARE_RESPONSE_QUEUE, cvCompareResponseMessage);

    }
}
