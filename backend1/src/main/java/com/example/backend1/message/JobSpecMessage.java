package com.example.backend1.message;

public class JobSpecMessage implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Long jobSpecId;
    private String cvContent;
    private String jobSpecContent;


    public JobSpecMessage() {
    }

    public JobSpecMessage(String cvContent, String jobSpecContent, Long jobSpecId) {
        this.cvContent = cvContent;
        this.jobSpecContent = jobSpecContent;
        this.jobSpecId = jobSpecId;
    }

    public Long getJobSpecId() {
        return jobSpecId;
    }

    public void setJobSpecId(Long jobSpecId) {
        this.jobSpecId = jobSpecId;
    }

    public String getCvContent() {
        return cvContent;
    }
    
    public void setCvContent(String cvContent) {
        this.cvContent = cvContent;
    }
    
    public String getJobSpecContent() {
        return jobSpecContent;
    }

    public void setJobSpecContent(String jobSpecContent) {
        this.jobSpecContent = jobSpecContent;
    }

}
