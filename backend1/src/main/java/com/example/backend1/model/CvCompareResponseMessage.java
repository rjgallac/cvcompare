package com.example.backend1.model;

import java.io.Serializable;

public class CvCompareResponseMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long jobSpecId;
    private int score;
    private String company;
    private String salary;
    private String title;
    private String location;

    public CvCompareResponseMessage() {
    }

    public CvCompareResponseMessage(Long jobSpecId, int score, String company, String salary, String title, String location) {
        this.jobSpecId = jobSpecId;
        this.score = score;
        this.company = company;
        this.salary = salary;
        this.title = title;
        this.location = location;
    }

    public Long getJobSpecId() {
        return jobSpecId;
    }
    public void setJobSpecId(Long jobSpecId) {
        this.jobSpecId = jobSpecId;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getCompany() {

        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getSalary() {
        return salary;
    }
    public void setSalary(String salary) {
        this.salary = salary;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    
}
