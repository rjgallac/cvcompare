package com.example.backend2.message;

public class CvCompareMessage implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Long cvCompareId;
    private String cvContent;
    private String jobSpecContent;

    public CvCompareMessage() {
    }

    

    public CvCompareMessage(Long cvCompareId, String cvContent, String jobSpecContent) {
        this.cvCompareId = cvCompareId;
        this.cvContent = cvContent;
        this.jobSpecContent = jobSpecContent;
    }



    public Long getCvCompareId() {
        return cvCompareId;
    }
    public void setCvCompareId(Long cvCompareId) {
        this.cvCompareId = cvCompareId;
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
