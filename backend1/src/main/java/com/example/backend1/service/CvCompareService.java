package com.example.backend1.service;

import org.springframework.stereotype.Service;

import com.example.backend1.dto.CompareCvDto;
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

    public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CvCompareService.class);

    public CvCompareService(CvCompareRepository cvCompareRepository, CvRepository cvRepository, JobSpecRepository jobSpecRepository) {
        this.cvCompareRepository = cvCompareRepository;
        this.cvRepository = cvRepository;
        this.jobSpecRepository = jobSpecRepository;
    }

    public void addCvCompare(CompareCvDto compareCvDto) {
        
        CurriculumVitae cv = cvRepository.findById(compareCvDto.getCvId()).get();
        JobSpec jobSpec = jobSpecRepository.findById(compareCvDto.getJobSpecId()).orElse(null);
        CvCompare cvCompare = new CvCompare();
      
        cvCompare.setCurriculumVitae(cv);
        cvCompare.setJobSpec(jobSpec);
        cvCompareRepository.save(cvCompare);
    }


}
