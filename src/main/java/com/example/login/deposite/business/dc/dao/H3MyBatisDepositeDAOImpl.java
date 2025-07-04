package com.example.login.deposite.business.dc.dao;

import com.example.login.deposite.business.dc.model.DMember;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Repository
public class H3MyBatisDepositeDAOImpl implements IH3DepositeDAO {
    private static Map<Long, DMember> members = new HashMap<>();

    @Override
    public DMember save(DMember member) {
        if (member.getId() == null) {
            member.setId((long) (members.size() + 1));
        }
        members.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<DMember> findById(Long id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public Optional<DMember> findByName(String name) {
        return members.values().stream()
                .filter(m -> m.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<DMember> findAll() {
        return new ArrayList<>(members.values());
    }
} 