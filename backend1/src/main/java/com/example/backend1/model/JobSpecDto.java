package com.example.backend1.model;

public class JobSpecDto {
    private Long id;
    private Long cvId;
    private String job_spec_content;
    private String location;
    private String salary;
    private String score;
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getSalary() {
        return salary;
    }
    public void setSalary(String salary) {
        this.salary = salary;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJob_spec_content() {
        return job_spec_content;
    }

    public void setJob_spec_content(String job_spec_content) {
        this.job_spec_content = job_spec_content;
    }
    public Long getCvId() {
        return cvId;
    }
    public void setCvId(Long cvId) {
        this.cvId = cvId;
    }
}
