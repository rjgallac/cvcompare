package com.example.backend1.model;

import org.springframework.stereotype.Component;

@Component
public class JobSpecMapper {

    public JobSpec toEntity(JobSpecDto jobSpecDto) {
        JobSpec jobSpec = new JobSpec();
        jobSpec.setId(jobSpecDto.getId());
        jobSpec.setJob_spec_content(jobSpecDto.getJob_spec_content());
        jobSpec.setLocation(jobSpecDto.getLocation());
        jobSpec.setSalary(jobSpecDto.getSalary());
        jobSpec.setScore(jobSpecDto.getScore());
        return jobSpec;
    }

    public JobSpecDto toDto(JobSpec jobSpec) {
        JobSpecDto jobSpecDto = new JobSpecDto();
        jobSpecDto.setId(jobSpec.getId());
        jobSpecDto.setJob_spec_content(jobSpec.getJob_spec_content());
        jobSpecDto.setLocation(jobSpec.getLocation());
        jobSpecDto.setSalary(jobSpec.getSalary());
        jobSpecDto.setScore(jobSpec.getScore());

        return jobSpecDto;
    }

}
