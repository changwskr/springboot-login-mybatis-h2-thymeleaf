package com.example.login.dto;

import com.example.login.model.User;

public class UserMapperUtil {
    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setToken(user.getToken());
        dto.setAddress(user.getAddress());
        dto.setAge(user.getAge());
        dto.setJob(user.getJob());
        dto.setCompany(user.getCompany());
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setToken(dto.getToken());
        user.setAddress(dto.getAddress());
        user.setAge(dto.getAge());
        user.setJob(dto.getJob());
        user.setCompany(dto.getCompany());
        return user;
    }
} 