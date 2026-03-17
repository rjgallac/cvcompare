package com.example.backend1.message;

import java.io.Serializable;

public class JobSpecResponseMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long jobSpecId;

    private String company;
    private String salary;
    private String title;
    private String location;

    public JobSpecResponseMessage() {
    }

    public JobSpecResponseMessage(Long jobSpecId, int score, String company, String salary, String title, String location) {
        this.jobSpecId = jobSpecId;
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
