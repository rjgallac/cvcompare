package com.example.backend1.message;

import java.io.Serializable;

public class CvResponseMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long cvId;
    private String suggestions;


    public CvResponseMessage() {
    }

    public CvResponseMessage(Long cvId, String suggestions) {
        this.cvId = cvId;
        this.suggestions = suggestions;
    }

    public Long getCvId() {
        return cvId;
    }
    public void setCvId(Long cvId) {
        this.cvId = cvId;
    }
    public String getSuggestions() {
        return suggestions;
    }
    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }
    
}
