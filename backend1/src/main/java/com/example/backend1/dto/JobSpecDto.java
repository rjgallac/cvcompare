package com.example.backend1.dto;

public class JobSpecDto {
    private Long id;
    private String name;
    private String job_spec_content;
    private String location;
    private String salary;
    private String score;
    private String jobTitle;
    private String company;
    private String status;

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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getJob_spec_content() {
        return job_spec_content;
    }

    public void setJob_spec_content(String job_spec_content) {
        this.job_spec_content = job_spec_content;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
