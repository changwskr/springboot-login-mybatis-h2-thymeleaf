package com.example.login.deposite.business.dc.dao;

import com.example.login.deposite.business.dc.dao.mapper.DepositeMapper;
import com.example.login.deposite.business.dc.model.DMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class H3MyBatisDepositeDAOImpl implements IH3DepositeDAO {

    private final DepositeMapper depositeMapper;

    @Autowired
    public H3MyBatisDepositeDAOImpl(DepositeMapper depositeMapper) {
        this.depositeMapper = depositeMapper;
    }

    @Override
    public DMember save(DMember member) {
        if (member.getId() == null) {
            depositeMapper.insertMember(member);
        } else {
            depositeMapper.updateMember(member);
        }
        return member;
    }

    @Override
    public Optional<DMember> findById(Long id) {
        return Optional.ofNullable(depositeMapper.selectMemberById(id));
    }

    @Override
    public Optional<DMember> findByName(String name) {
        List<DMember> list = depositeMapper.selectMembersByName(name);
        return list.stream().findFirst();
    }

    @Override
    public List<DMember> findAll() {
        return depositeMapper.selectAllMembers();
    }
} 