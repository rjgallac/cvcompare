package com.example.backend1.mapper;

import org.springframework.stereotype.Component;

import com.example.backend1.dto.JobSpecDto;
import com.example.backend1.model.JobSpec;

@Component
public class JobSpecMapper {

    public JobSpec toEntity(JobSpecDto jobSpecDto) {
        JobSpec jobSpec = new JobSpec();
        jobSpec.setId(jobSpecDto.getId());
        jobSpec.setJobSpecContent(jobSpecDto.getJob_spec_content());
        jobSpec.setLocation(jobSpecDto.getLocation());
        jobSpec.setSalary(jobSpecDto.getSalary());
        return jobSpec;
    }

    public JobSpecDto toDto(JobSpec jobSpec) {
        JobSpecDto jobSpecDto = new JobSpecDto();
        jobSpecDto.setId(jobSpec.getId());
        jobSpecDto.setJob_spec_content(jobSpec.getJobSpecContent());
        jobSpecDto.setLocation(jobSpec.getLocation());
        jobSpecDto.setSalary(jobSpec.getSalary());
        jobSpecDto.setJobTitle(jobSpec.getJobTitle());
        jobSpecDto.setCompany(jobSpec.getCompany());
        jobSpecDto.setStatus(jobSpec.getStatus());
        return jobSpecDto;
    }

}
