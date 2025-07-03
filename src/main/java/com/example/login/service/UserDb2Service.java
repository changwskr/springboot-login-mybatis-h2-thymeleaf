package com.example.login.service;

import com.example.login.mapper.db2.UserMapperDb2;
import com.example.login.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDb2Service {
    @Autowired
    private UserMapperDb2 userMapper;

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void save(User user) {
        userMapper.insert(user);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public void delete(String username) {
        userMapper.delete(username);
    }
} 