package com.example.backend1.dto;

public class CompareCvListItemDto {
    private Long id;
    private String cvName;
    private String jobSpecName;
    private String compareContent;
    private int score;
    private String status;


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
    public String getCompareContent() {
        return compareContent;
    }
    public void setCompareContent(String compareContent) {
        this.compareContent = compareContent;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
