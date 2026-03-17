package com.example.backend1.service;


import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend1.config.QueueVars;
import com.example.backend1.controllers.CvController;
import com.example.backend1.dto.CvDto;
import com.example.backend1.mapper.CvMapper;
import com.example.backend1.message.CvMessage;
import com.example.backend1.model.CurriculumVitae;
import com.example.backend1.repository.CvRepository;
import java.util.logging.Logger;

@Service
public class CvService {

    private final RabbitTemplate rabbitTemplate;

    private final CvRepository cvRepository;
    private final CvMapper cvMapper;

    Logger logger = Logger.getLogger(CvService.class.getName());


    public CvService(RabbitTemplate rabbitTemplate, CvRepository cvRepository, CvMapper cvMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.cvRepository = cvRepository;
        this.cvMapper = cvMapper;
    }

    public void addCv(CvDto cvDto) {
        CurriculumVitae curriculumVitae = cvMapper.toEntity(cvDto);
        curriculumVitae.setStatus("pending");
        CurriculumVitae saved = cvRepository.save(curriculumVitae);
        logger.info("Curriculum Vitae added: " + curriculumVitae.getId());

        CvMessage cvMessage = new CvMessage(curriculumVitae.getCurriculum_vitae_content(), saved.getId());
        rabbitTemplate.convertAndSend(QueueVars.CV_QUEUE, cvMessage);
    }

    public List<CurriculumVitae> getCvs() {
        return cvRepository.findAll();
    }

    public CurriculumVitae getCv(Long id) {
        return cvRepository.findById(id).orElse(null);
    }

    public void deleteCv(Long id) {
        cvRepository.deleteById(id);
    }

}
