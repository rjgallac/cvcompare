package com.example.backend1.dto;

public class CompareCvDto {

    private Long cvId;
    private Long jobSpecId;

    public Long getCvId() {
        return cvId;
    }
    public void setCvId(Long cvId) {
        this.cvId = cvId;
    }
    public Long getJobSpecId() {
        return jobSpecId;
    }
    public void setJobSpecId(Long jobSpecId) {
        this.jobSpecId = jobSpecId;
    }

}
