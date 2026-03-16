package com.example.backend1.dto;

public class CompareCvListItemDto {
    private Long id;
    private String cvName;
    private String jobSpecName;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCvName() {
        return cvName;
    }
    public void setCvName(String cvName) {
        this.cvName = cvName;
    }
    public String getJobSpecName() {
        return jobSpecName;
    }
    public void setJobSpecName(String jobSpecName) {
        this.jobSpecName = jobSpecName;
    }

}
