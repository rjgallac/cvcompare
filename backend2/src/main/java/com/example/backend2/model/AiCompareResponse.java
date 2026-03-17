package com.example.backend2.model;

public class AiCompareResponse {
    private int score;
    private String comparison;


    public AiCompareResponse() {
    }

    public AiCompareResponse(int score, String comparison) {
        this.score = score;
        this.comparison = comparison;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    

}
