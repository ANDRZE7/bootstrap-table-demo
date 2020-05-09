package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.datatable.DataTableResponseBuilder;
import com.example.bootstraptabledemo.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;

@Service
@Scope("prototype")
public class DataTableQueryService {

    public static final String COUNT_TOTAL = "total";
    public static final String COUNT_FILTERED = "filtered";

    // Persistance EntityManager injected in constructor.
    //    @PersistenceContext
    //    private EntityManager entityManager;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<Person> criteriaQuery;
    private final Root<Person> from;

    @Autowired
    public DataTableQueryService(EntityManager entityManager) {
        this.entityManager = entityManager;
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Person.class);
        from = criteriaQuery.from(Person.class);
    }

    public DataTableResponse query(DataTableQueryParameters parameters) {
        DataTableQueryResult result  = this.executeQuery(parameters);
        DataTableResponse dataTableResponse = DataTableResponseBuilder.builder()
                .draw(parameters.getDraw())
                .recordsFiltered(new Long(result.filteredResult.size()))
                .recordsFiltered(result.recordFiltered)
                .recordsTotal(result.recordsTotal)
                .data(result.filteredResult)
                .build();
        return dataTableResponse;
    }

    private DataTableQueryResult executeQuery(DataTableQueryParameters parameters) {
        // apply where
        Predicate wherePredicate = constructWherePredicate(parameters);
        if(wherePredicate != null)
            criteriaQuery.where(wherePredicate);

        // apply order
        List<Order> orderList = constructOrderList(parameters);
        if(orderList.size() > 0)
            criteriaQuery.orderBy(orderList);

        // count records
        Map<String, Long> count = getCount(wherePredicate);

        // apply paging
        Query query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(parameters.getStart());
        query.setMaxResults(parameters.getLength());

        List<Person> persons = query.getResultList();

        return new DataTableQueryResult(
                count.entrySet().stream().filter(c -> c.getKey().equals(COUNT_TOTAL)).map(Map.Entry::getValue).findFirst().get(),
                count.entrySet().stream().filter(c -> c.getKey().equals(COUNT_FILTERED)).map(Map.Entry::getValue).findFirst().get(),
                persons);
    }

    private Map<String, Long> getCount(Predicate where) {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Person.class)));

        // total records without filter applied
        final Long totalRecords = entityManager.createQuery(cq).getSingleResult();


        // total records with where predicate applied
        cq.where(where);
        final Long recordsFiltered = where == null ? totalRecords : entityManager.createQuery(cq).getSingleResult();

        // construct and return the result
        return new HashMap<String, Long>() {
            {
                put(COUNT_TOTAL, totalRecords);
                put(COUNT_FILTERED, recordsFiltered);
            }
        };
    }

    private Predicate constructWherePredicate(final DataTableQueryParameters parameters) {
        // where predicates
        if(!parameters.getSearchValue().isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            parameters.getColumnInfos().stream().filter(ci -> ci.getSearchable()).forEach(c -> {
                String columnName = c.getData(); /* this holds column name for now, TODO: fix how the column name is stored in data table query parameters */
                // Converting all to string, consider to use a datatype mapping in the future
                Predicate predicate = criteriaBuilder.like(from.get(columnName).as(String.class), parameters.getSearchValue());
                // % must be passed explicitly, to this is commented out: String.format("%%%s%%", parameters.getSearchValue()));
                predicates.add(predicate);
            });
            return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
        }
        return null;
    }

    private List<Order> constructOrderList(final DataTableQueryParameters parameters) {
        // analyze ordering
        List<Order> orderList = new ArrayList<>();
        parameters.getColumnOrders().forEach(co -> {
            // select column name
            Optional<String> columnName = parameters.getColumnInfos().stream()
                    .filter(ci -> ci.getIndex().equals(co.getColumn()))
                    .map(ci -> ci.getData()).findFirst();
            // determine type of sorting
            if(columnName.isPresent())
                if(co.getDir().equals("asc"))
                    orderList.add(criteriaBuilder.asc(from.get(columnName.get())));
                else
                    orderList.add(criteriaBuilder.desc(from.get(columnName.get())));
        });
        return orderList;
    }

    private class DataTableQueryResult {
        // number of all records in the queried table: select * from the table
        private final Long  recordsTotal;

        // number or records that would be returned after applying the where predicates without the top for paging
        private final Long recordFiltered;

        // result set to be returned to client; after applying where and top for paging, so it's just one page of data
        private final List<Person> filteredResult;

        public DataTableQueryResult(Long recordsTotal, Long recordFiltered, List<Person> filteredResult) {
            this.recordsTotal = recordsTotal;
            this.recordFiltered = recordFiltered;
            this.filteredResult = filteredResult;
        }
    }
}
