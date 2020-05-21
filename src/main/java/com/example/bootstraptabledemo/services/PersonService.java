package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableSevice;
import com.example.bootstraptabledemo.domain.Person;

public interface PersonService extends DataTableSevice {

    Iterable<Person> findAll(int limit);

}
