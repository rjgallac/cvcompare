package com.example.backend1.consumer;

import java.util.logging.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.backend1.message.JobSpecResponseMessage;
import com.example.backend1.config.QueueVars;
import com.example.backend1.config.RabbitMQConfig;
import com.example.backend1.message.CvCompareResponseMessage;
import com.example.backend1.message.CvResponseMessage;
import com.example.backend1.model.CurriculumVitae;
import com.example.backend1.model.CvCompare;
import com.example.backend1.model.JobSpec;
import com.example.backend1.repository.CvCompareRepository;
import com.example.backend1.repository.CvRepository;
import com.example.backend1.repository.JobSpecRepository;

@Component
public class StatusConsumer {

    Logger logger = Logger.getLogger(StatusConsumer.class.getName());
    
    private final SimpMessagingTemplate messagingTemplate;

    private final JobSpecRepository jobSpecRepository;

    private final CvRepository cvRepository;

    private final CvCompareRepository cvCompareRepository;

    public StatusConsumer(SimpMessagingTemplate messagingTemplate, JobSpecRepository jobSpecRepository, CvRepository cvRepository, CvCompareRepository cvCompareRepository) {
        this.messagingTemplate = messagingTemplate;
        this.jobSpecRepository = jobSpecRepository;
        this.cvRepository = cvRepository;
        this.cvCompareRepository = cvCompareRepository;
    }

    @RabbitListener(queues = QueueVars.CV_RESPONSE_QUEUE)
    public void receiveSuggestMessage(CvResponseMessage cvSuggestResponseMessage) {
        logger.info("Received suggest message: " + cvSuggestResponseMessage.getCvId());
        CurriculumVitae curriculumVitae = cvRepository.findById(cvSuggestResponseMessage.getCvId()).get();
        curriculumVitae.setCurriculum_vitae_content_suggestions(cvSuggestResponseMessage.getSuggestions());
        cvRepository.save(curriculumVitae);
    }

    @RabbitListener(queues = QueueVars.JOBSPEC_RESPONSE_QUEUE)
    public void receiveMessage(JobSpecResponseMessage message) {
        logger.info("Received status message: " + message.getJobSpecId() );
        JobSpec jobSpec = jobSpecRepository.findById(message.getJobSpecId()).get();
        jobSpec.setLocation(message.getLocation().substring(0, Math.min(200, message.getLocation().length())));
        jobSpec.setJobTitle(message.getTitle().substring(0, Math.min(200, message.getTitle().length())));
        jobSpec.setCompany(message.getCompany().substring(0, Math.min(200, message  .getCompany().length())));
        jobSpec.setSalary(message.getSalary());
        jobSpec.setStatus("completed");
        jobSpecRepository.save(jobSpec);
        messagingTemplate.convertAndSend("/topic/status", message);
    }

    @RabbitListener(queues = QueueVars.CV_COMPARE_RESPONSE_QUEUE)
    public void receiveMessage(CvCompareResponseMessage cvCompareResponseMessage) {
        logger.info("Received status message: " + cvCompareResponseMessage.getCvCompareId() );
        CvCompare cvCompare = cvCompareRepository.findById( cvCompareResponseMessage.getCvCompareId()).get();
        cvCompare.setCompareContent(cvCompareResponseMessage.getCompareContent().substring(0, Math.min(200, cvCompareResponseMessage.getCompareContent().length())));
        cvCompare.setScore(cvCompareResponseMessage.getScore());
        cvCompare.setStatus("completed");
        cvCompareRepository.save(cvCompare);
        messagingTemplate.convertAndSend("/topic/status", cvCompare);
    }

}
