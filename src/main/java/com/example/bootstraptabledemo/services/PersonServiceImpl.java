package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Iterable<Person> findAll() {
        return this.personRepository.findAll();
    }
}
