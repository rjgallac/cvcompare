package com.example.backend1.model;

public class CvDto {
    private Long id;
    private String name;
    private String curriculum_vitae_content;

    // Getters and setters
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
    public String getCurriculum_vitae_content() {
        return curriculum_vitae_content;
    }
    public void setCurriculum_vitae_content(String curriculum_vitae_content) {
        this.curriculum_vitae_content = curriculum_vitae_content;
    }

}
