package com.example.backend1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_spec")
public class JobSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String job_title;

    private String job_spec_content;

    private String location;

    private String salary;

    private String score;

    private Long cvId;

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getJob_title() {
        return job_title;
    }
    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }
    public String getJob_spec_content() {
        return job_spec_content;
    }
    public void setJob_spec_content(String job_spec_content) {
        this.job_spec_content = job_spec_content;
    }
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

    public Long getCvId() {
        return cvId;
    }

    public void setCvId(Long cvId) {
        this.cvId = cvId;
    }



}
