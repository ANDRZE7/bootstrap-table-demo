package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.domain.Person;

public interface PersonService {
    Iterable<Person> findAll();
}
