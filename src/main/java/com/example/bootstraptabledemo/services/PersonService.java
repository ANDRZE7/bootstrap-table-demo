package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.TableDataSevice;
import com.example.bootstraptabledemo.domain.Person;

public interface PersonService extends TableDataSevice {

    Iterable<Person> findAll();

}
