package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.datatable.DataTableResponseBuilder;
import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PersonServiceImpl implements PersonService {

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
        DataTableQueryResult result  = this.executeQuery(parameters);
        DataTableResponse dataTableResponse = DataTableResponseBuilder.builder()
                .draw(parameters.getDraw())
//                .recordsFiltered(result.filteredResult.size())
                .recordsFiltered(result.reordsTotal)
                .recordsTotal(result.reordsTotal)
                .data(result.filteredResult)
                .build();
        return dataTableResponse;
    }

    private DataTableQueryResult executeQuery(DataTableQueryParameters parameters) {
        List<Person> result = personRepository.findTop10ByOrderByIdDesc();
        return new DataTableQueryResult(100, result);
    }

    private class DataTableQueryResult {
        private final int reordsTotal;
        private final List<Person> filteredResult;

        public DataTableQueryResult(int reordsTotal, List<Person> filteredResult) {
            this.reordsTotal = reordsTotal;
            this.filteredResult = filteredResult;
        }
    }
}
