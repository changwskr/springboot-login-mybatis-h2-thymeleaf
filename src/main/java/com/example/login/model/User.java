package com.example.login.model;

import javax.validation.constraints.*;

public class User {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String address;

    @Min(0)
    @Max(120)
    private Integer age;

    private String job;
    private String company;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
}
