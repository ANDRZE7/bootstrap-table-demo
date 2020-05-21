package com.example.bootstraptabledemo.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Person> findOrderedByIdLimitedTo(int limit) {
        return entityManager.createQuery("SELECT p FROM Person p ORDER BY p.id",
                Person.class).setMaxResults(limit).getResultList();
    }
}
