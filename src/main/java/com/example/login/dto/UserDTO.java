package com.example.login.dto;

public class UserDTO {
    private String username;
    private String password;
    private String token;
    private String address;
    private Integer age;
    private String job;
    private String company;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
} 