package com.example.login.deposite.business.dc.dao;

import com.example.login.deposite.business.dc.model.DMember;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.HashMap;

@Repository
public class H3MEMDepositeDAOImpl implements IH3DepositeDAO {
    private static Map<Long, DMember> store = new HashMap<>();
    private static AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public DMember save(DMember member) {
        if (member.getId() == null) {
            member.setId(idGenerator.getAndIncrement());
        }
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<DMember> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<DMember> findByName(String name) {
        return store.values().stream()
                .filter(m -> m.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<DMember> findAll() {
        return new ArrayList<>(store.values());
    }
} 