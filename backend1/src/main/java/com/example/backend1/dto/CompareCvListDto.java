package com.example.backend1.dto;

public class CompareCvListDto {
    private Long id;
    private CvDto cvDto;
    private JobSpecDto jobSpecDto;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public CvDto getCvDto() {
        return cvDto;
    }
    public void setCvDto(CvDto cvDto) {
        this.cvDto = cvDto;
    }
    public JobSpecDto getJobSpecDto() {
        return jobSpecDto;
    }
    public void setJobSpecDto(JobSpecDto jobSpecDto) {
        this.jobSpecDto = jobSpecDto;
    }
    public CompareCvListDto() {
    }

}
