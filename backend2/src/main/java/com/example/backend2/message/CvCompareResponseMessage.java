package com.example.backend2.message;

public class CvCompareResponseMessage  implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Long cvCompareId;
    private int score;
    private String compareContent;
    
    public CvCompareResponseMessage() {
    }

    

    public CvCompareResponseMessage(Long cvCompareId, int score, String compareContent) {
        this.cvCompareId = cvCompareId;
        this.score = score;
        this.compareContent = compareContent;
    }



    public Long getCvCompareId() {
        return cvCompareId;
    }

    public void setCvCompareId(Long cvCompareId) {
        this.cvCompareId = cvCompareId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCompareContent() {
        return compareContent;
    }

    public void setCompareContent(String compareContent) {
        this.compareContent = compareContent;
    }

}
