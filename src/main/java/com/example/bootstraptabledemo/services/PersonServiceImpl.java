package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.datatable.DataTableResponseBuilder;
import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class PersonServiceImpl implements PersonService {

    @PersistenceContext
    private EntityManager entityManager;

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
                .recordsFiltered(result.recordsTotal)
                .recordsTotal(result.recordsTotal)
                .data(result.filteredResult)
                .build();
        return dataTableResponse;
    }

    private DataTableQueryResult executeQuery(DataTableQueryParameters parameters) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> from = cq.from(Person.class);

        // where predicates
        if(!parameters.getSearchValue().isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            parameters.getColumnInfos().stream().filter(ci -> ci.getSearchable()).forEach(c -> {
                String columnName = c.getData(); /* this holds column name for now, TODO: fix how the column name is stored in data table query parameters */
                // Converting all to string, consider to use a datatype mapping in the future
                Predicate predicate = cb.like(from.get(columnName).as(String.class), parameters.getSearchValue());
                // % must be passed explicitly, to this is commented out: String.format("%%%s%%", parameters.getSearchValue()));
                predicates.add(predicate);
            });
            Predicate finalPredicate;
            finalPredicate = cb.or(predicates.toArray(new Predicate[predicates.size()]));
            cq.where(finalPredicate);
        }

        // apply ordering
        List<Order> orderList = new ArrayList<>();
        parameters.getColumnOrders().forEach(co -> {
            // select column name
            Optional<String> columnName = parameters.getColumnInfos().stream()
                    .filter(ci -> ci.getIndex().equals(co.getColumn()))
                    .map(ci -> ci.getData()).findFirst();

            // determine type of sorting
            if(columnName.isPresent())
            if(co.getDir().equals("asc"))
                orderList.add(cb.asc(from.get(columnName.get())));
            else
                orderList.add(cb.desc(from.get(columnName.get())));

        });
        if(orderList.size()>0)
            cq.orderBy(orderList);

        List<Person> persons = entityManager.createQuery(cq).getResultList();
        return new DataTableQueryResult(100, persons);
    }

    private class DataTableQueryResult {
        private final int recordsTotal;
        private final List<Person> filteredResult;

        public DataTableQueryResult(int recordsTotal, List<Person> filteredResult) {
            this.recordsTotal = recordsTotal;
            this.filteredResult = filteredResult;
        }
    }
}
