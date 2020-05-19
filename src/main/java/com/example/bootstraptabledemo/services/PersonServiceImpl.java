package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
class PersonServiceImpl implements PersonService {

    @PersistenceContext
    EntityManager entityManager;

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Iterable<Person> findAll() {
        return this.personRepository.findAll();
    }

    @Override
    public DataTableResponse query(DataTableQueryParameters parameters) {
        DataTableQueryExecutor service = new DataTableQueryExecutor<Person>(Person.class);
        return service.query(entityManager, parameters);
    }
}
