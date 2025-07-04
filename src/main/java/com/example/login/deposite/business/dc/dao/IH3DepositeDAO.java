package com.example.login.deposite.business.dc.dao;

import com.example.login.deposite.business.dc.model.DMember;

import java.util.List;
import java.util.Optional;

public interface IH3DepositeDAO {
    DMember save(DMember member);
    Optional<DMember> findById(Long id);
    Optional<DMember> findByName(String name);
    List<DMember> findAll();
} 