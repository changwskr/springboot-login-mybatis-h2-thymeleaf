package com.example.login.deposite.business.dc.dao;

import com.example.login.deposite.business.dc.model.DMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

//@Repository
public class H3JPADepositeDAOImpl implements IH3DepositeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DMember save(DMember member) {
        if (member.getId() == null) {
            entityManager.persist(member);
            return member;
        } else {
            return entityManager.merge(member);
        }
    }

    @Override
    public Optional<DMember> findById(Long id) {
        return Optional.ofNullable(entityManager.find(DMember.class, id));
    }

    @Override
    public Optional<DMember> findByName(String name) {
        TypedQuery<DMember> query = entityManager.createQuery(
                "SELECT m FROM DMember m WHERE m.name = :name", DMember.class);
        query.setParameter("name", name);
        List<DMember> result = query.getResultList();
        return result.stream().findFirst();
    }

    @Override
    public List<DMember> findAll() {
        TypedQuery<DMember> query = entityManager.createQuery(
                "SELECT m FROM DMember m", DMember.class);
        return query.getResultList();
    }
} 