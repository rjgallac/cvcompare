package com.example.backend1.service;


import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend1.config.QueueVars;
import com.example.backend1.message.CvMessage;
import com.example.backend1.model.CurriculumVitae;
import com.example.backend1.repository.CvRepository;

@Service
public class CvService {

    private final RabbitTemplate rabbitTemplate;

    private final CvRepository cvRepository;

    public CvService(RabbitTemplate rabbitTemplate, CvRepository cvRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.cvRepository = cvRepository;
    }

    public void addCv(CurriculumVitae curriculumVitae) {
        CurriculumVitae saved = cvRepository.save(curriculumVitae);
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

    public CurriculumVitae sendCvForComparison(Long cvId) {
        CurriculumVitae cv = getCv(cvId);
        cv.setStatus("Processing");
        cvRepository.save(cv);
        CvMessage cvSuggestMessage = new CvMessage(cv.getCurriculum_vitae_content(), cvId);
        rabbitTemplate.convertAndSend("cv-suggest-queue", cvSuggestMessage);
        return cv;
    }

}
