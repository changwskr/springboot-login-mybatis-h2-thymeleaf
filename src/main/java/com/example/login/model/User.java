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

    private String token;

    // DB 구분을 위한 필드 추가
    private String dbName;  // "DB1" 또는 "DB2"

    // 생성자
    public User() {
        // 기본 생성자
    }

    public User(String username, String password, String dbName) {
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }

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

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getDbName() { return dbName; }
    public void setDbName(String dbName) { this.dbName = dbName; }

    /**
     * DB명에 따른 표시용 문자열 반환
     */
    public String getDbDisplayName() {
        if ("DB1".equals(dbName)) {
            return "1번 DB";
        } else if ("DB2".equals(dbName)) {
            return "2번 DB";
        } else {
            return dbName;
        }
    }

    /**
     * 사용자 정보 문자열 표현
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                ", job='" + job + '\'' +
                ", company='" + company + '\'' +
                ", token='" + token + '\'' +
                ", dbName='" + dbName + '\'' +
                '}';
    }
}
