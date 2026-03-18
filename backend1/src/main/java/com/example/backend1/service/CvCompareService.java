package com.example.backend1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.backend1.config.QueueVars;
import com.example.backend1.dto.CompareCvDto;
import com.example.backend1.dto.CompareCvListItemDto;
import com.example.backend1.message.CvCompareMessage;
import com.example.backend1.message.JobSpecMessage;
import com.example.backend1.model.CurriculumVitae;
import com.example.backend1.model.CvCompare;
import com.example.backend1.model.JobSpec;
import com.example.backend1.repository.CvCompareRepository;
import com.example.backend1.repository.CvRepository;
import com.example.backend1.repository.JobSpecRepository;

@Service
public class CvCompareService {

    private final CvCompareRepository cvCompareRepository;

    private final CvRepository cvRepository;

    private final JobSpecRepository jobSpecRepository;

    private final RabbitTemplate rabbitTemplate;

    public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CvCompareService.class);

    public CvCompareService(CvCompareRepository cvCompareRepository, CvRepository cvRepository, JobSpecRepository jobSpecRepository, RabbitTemplate rabbitTemplate) {
        this.cvCompareRepository = cvCompareRepository;
        this.cvRepository = cvRepository;
        this.jobSpecRepository = jobSpecRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void addCvCompare(CompareCvDto compareCvDto) {
        
        CurriculumVitae cv = cvRepository.findById(compareCvDto.getCvId()).get();
        JobSpec jobSpec = jobSpecRepository.findById(compareCvDto.getJobSpecId()).orElse(null);
        CvCompare cvCompare = new CvCompare();
      
        cvCompare.setCurriculumVitae(cv);
        cvCompare.setJobSpec(jobSpec);
        CvCompare save = cvCompareRepository.save(cvCompare);
        // send compare to rabbitmq for processing by ai backend

        CvCompareMessage cvCompareMessage = new CvCompareMessage();
        cvCompareMessage.setCvCompareId(save.getId());
        cvCompareMessage.setCvContent(save.getCurriculumVitae().getCurriculum_vitae_content());
        cvCompareMessage.setJobSpecContent(save.getJobSpec().getJobSpecContent());
        

        // send CV and JobSpec to RabbitMQ
        rabbitTemplate.convertAndSend(QueueVars.CV_COMPARE_QUEUE, cvCompareMessage);

    }

    public List<CompareCvListItemDto> getAllCvCompares() {
        List<CvCompare> cvCompares = (List<CvCompare>) cvCompareRepository.findAll();
        List<CompareCvListItemDto> compareCvListItemDtos = new ArrayList<>();
        for (CvCompare cvCompare : cvCompares) {
            CompareCvListItemDto itemDto = new CompareCvListItemDto();
            itemDto.setId(cvCompare.getId());
            itemDto.setCvName(cvCompare.getCurriculumVitae().getName());
            itemDto.setJobSpecName(cvCompare.getJobSpec().getName());
            itemDto.setCompareContent(cvCompare.getCompareContent());
            itemDto.setScore(cvCompare.getScore());
            itemDto.setStatus(cvCompare.getStatus());
            compareCvListItemDtos.add(itemDto);
        }
        return compareCvListItemDtos;
    }

    public void delete(Long id) {
       cvCompareRepository.deleteById(id);
    }


}
