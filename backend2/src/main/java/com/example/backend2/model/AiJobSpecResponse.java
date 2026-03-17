package com.example.backend2.model;

public class AiJobSpecResponse {
    private String company;
    private String salary;
    private String title;
    private String location;

    public AiJobSpecResponse() {
    }

    public AiJobSpecResponse(String company, String salary, String title, String location) {
        this.company = company;
        this.salary = salary;
        this.title = title;
        this.location = location;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
