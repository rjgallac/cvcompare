package com.example.backend1.consumer;

import java.util.logging.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.backend1.model.CvCompareResponseMessage;
import com.example.backend1.model.JobSpec;
import com.example.backend1.model.StatusMessage;
import com.example.backend1.repository.JobSpecRepository;

@Component
public class StatusConsumer {

    Logger logger = Logger.getLogger(StatusConsumer.class.getName());
    
    private final SimpMessagingTemplate messagingTemplate;

    private final JobSpecRepository jobSpecRepository;

    public StatusConsumer(SimpMessagingTemplate messagingTemplate, JobSpecRepository jobSpecRepository) {
        this.messagingTemplate = messagingTemplate;
        this.jobSpecRepository = jobSpecRepository;
    }

    @RabbitListener(queues = "status-queue")
    public void receiveMessage(CvCompareResponseMessage message) {
        logger.info("Received status message: " + message.getJobSpecId() + " with status: " + message.getScore());
        JobSpec jobSpec = jobSpecRepository.findById(message.getJobSpecId()).get();
        jobSpec.setScore(String.valueOf(message.getScore()));
        jobSpec.setLocation(message.getLocation().substring(0, Math.min(200, message.getLocation().length())));
        jobSpec.setJobTitle(message.getTitle().substring(0, Math.min(200, message.getTitle().length())));
        jobSpec.setCompany(message.getCompany().substring(0, Math.min(200, message  .getCompany().length())));
        jobSpec.setSalary(message.getSalary());
        jobSpec.setStatus("completed");
        jobSpecRepository.save(jobSpec);
        messagingTemplate.convertAndSend("/topic/status", message);
    }
}
