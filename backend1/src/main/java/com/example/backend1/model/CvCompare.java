package com.example.backend1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "cv_compare")
public class CvCompare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CurriculumVitae curriculumVitae;

    @ManyToOne
    @JoinColumn(name = "job_spec_id")
    private JobSpec jobSpec;

    @Column(columnDefinition="TEXT")
    private String compareContent;

    private int score;

    private String status;

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public CurriculumVitae getCurriculumVitae() {
        return curriculumVitae;
    }
    public void setCurriculumVitae(CurriculumVitae curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }
    public JobSpec getJobSpec() {
        return jobSpec;
    }
    public void setJobSpec(JobSpec jobSpec) {
        this.jobSpec = jobSpec;
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
