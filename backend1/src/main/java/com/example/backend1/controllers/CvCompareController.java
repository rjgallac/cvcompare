package com.example.backend1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend1.dto.CompareCvDto;
import com.example.backend1.model.CurriculumVitae;
import com.example.backend1.model.CvCompare;
import com.example.backend1.model.JobSpec;
import com.example.backend1.repository.CvCompareRepository;
import com.example.backend1.repository.CvRepository;
import com.example.backend1.repository.JobSpecRepository;
import com.example.backend1.service.CvCompareService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/compare")
public class CvCompareController {

    private final CvCompareService cvCompareService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CvCompareController.class);


    public CvCompareController(CvCompareService cvCompareService) {
        this.cvCompareService = cvCompareService;
    }

    @PostMapping()
    public String addCvCompare(@RequestBody CompareCvDto compareCvDto) {
       cvCompareService.addCvCompare(compareCvDto);

        return "    ";
    }
    

}
