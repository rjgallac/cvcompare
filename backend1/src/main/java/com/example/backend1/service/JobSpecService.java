package com.example.backend1.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.backend1.config.QueueVars;
import com.example.backend1.message.JobSpecMessage;
import com.example.backend1.model.CurriculumVitae;
import com.example.backend1.model.JobSpec;
import com.example.backend1.repository.CvRepository;
import com.example.backend1.repository.JobSpecRepository;

@Service
public class JobSpecService {

    private final RabbitTemplate rabbitTemplate;

    private final JobSpecRepository jobSpecRepository;

    private final CvRepository cvRepository;

    public JobSpecService(JobSpecRepository jobSpecRepository, CvRepository cvRepository, RabbitTemplate rabbitTemplate) {
        this.jobSpecRepository = jobSpecRepository;
        this.cvRepository = cvRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<JobSpec> getJobSpecs() {
        return jobSpecRepository.findAll();
    }

    public void addJobSpec(JobSpec jobSpec) {
        JobSpec savedJobSpec = jobSpecRepository.save(jobSpec);


        JobSpecMessage jobSpecMessage = new JobSpecMessage();
        jobSpecMessage.setJobSpecId(savedJobSpec.getId());
        jobSpecMessage.setJobSpecContent(savedJobSpec.getJobSpecContent());
        // cvCompareMessage.setCvContent(cv.getCurriculum_vitae_content());
        

        // send CV and JobSpec to RabbitMQ
        rabbitTemplate.convertAndSend(QueueVars.JOBSPEC_QUEUE, jobSpecMessage);
        // logger.info("Product added: " + product.getName() + " with price: " + product.getPrice() + " and status: " + product.getStatus());

    }

    public void deleteJobSpec(Long id) {
        jobSpecRepository.deleteById(id);
    }

    

}
