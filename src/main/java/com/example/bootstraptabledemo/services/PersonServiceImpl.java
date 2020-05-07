package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

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

    @Override
    public DataTableResponse query(DataTableQueryParameters parameters) {
        List<Person> results = this.executeQuery(parameters);
        DataTableResponse dataTableResponse = DataTableResponse.builder()
                .draw(parameters.getDraw())
                .recordsFiltered(results.size())
                .recordsTotal(results.size())
                .data(results)
                .build();
        return dataTableResponse;
    }

    private List<Person> executeQuery(DataTableQueryParameters parameters) {
        throw new NotImplementedException();
    }
}
