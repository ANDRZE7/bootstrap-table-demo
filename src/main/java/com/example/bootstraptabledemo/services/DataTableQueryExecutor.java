package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.datatable.DataTableResponseBuilder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;

public class DataTableQueryExecutor<T> {

    public static final String COUNT_TOTAL = "total";
    public static final String COUNT_FILTERED = "filtered";

    Class c;

    public DataTableQueryExecutor(Class<T> c) {
        this.c = c;
    }

    private class QueryContext<T> {
        private final EntityManager entityManager;
        private final CriteriaBuilder criteriaBuilder;
        private final CriteriaQuery<T> criteriaQuery;
        private final Root<T> from;
        public QueryContext(EntityManager entityManager) {
            this.entityManager = entityManager;
            criteriaBuilder = entityManager.getCriteriaBuilder();
            criteriaQuery = criteriaBuilder.createQuery(c);
            from = criteriaQuery.from(c);
        }
    }

    public DataTableResponse query(EntityManager entityManager, DataTableQueryParameters parameters, int limit) {
        QueryContext queryContext = new QueryContext<T>(entityManager);
        DataTableQueryResult result  = this.executeQuery(queryContext, parameters, limit);
        DataTableResponse dataTableResponse = DataTableResponseBuilder.builder()
                .draw(parameters.getDraw())
                .recordsFiltered(new Long(result.filteredResult.size()))
                .recordsFiltered(result.recordFiltered)
                .recordsTotal(result.recordsTotal)
                .data(result.filteredResult)
                .build();
        return dataTableResponse;
    }

    private DataTableQueryResult executeQuery(QueryContext context, DataTableQueryParameters parameters, int rowsetLimit) {
        // apply where
        Predicate limit = constructLimitPredicete(context, rowsetLimit);
        Predicate where = constructWherePredicate(context, parameters);

        if(where != null)
            context.criteriaQuery.where(where, limit);
        else
            context.criteriaQuery.where(limit);

        // apply order
        List<Order> orderList = constructOrderList(context, parameters);
        if(orderList.size() > 0)
            context.criteriaQuery.orderBy(orderList);

        // count records
        Map<String, Long> count = getCount(context, where, limit);

        // apply paging
        Query query = context.entityManager.createQuery(context.criteriaQuery);
        query.setFirstResult(parameters.getStart());
        query.setMaxResults(parameters.getLength());

        List<T> objectList = query.getResultList();

        return new DataTableQueryResult(
                count.entrySet().stream().filter(c -> c.getKey().equals(COUNT_TOTAL)).map(Map.Entry::getValue).findFirst().get(),
                count.entrySet().stream().filter(c -> c.getKey().equals(COUNT_FILTERED)).map(Map.Entry::getValue).findFirst().get(),
                objectList);
    }

    private Map<String, Long> getCount(QueryContext context, Predicate where, Predicate limit) {
        CriteriaQuery<Long> cq = context.criteriaBuilder.createQuery(Long.class);
        cq.select(context.criteriaBuilder.count(cq.from(c)));

        // total records without filter applied
        // TODO: This throws error: cq.where(limit);
        final Long totalRecords = context.entityManager.createQuery(cq).getSingleResult();

        // total records with where predicate applied
        // cq.where(where, limit);
        cq.where(where);
        final Long recordsFiltered = where == null ? totalRecords : context.entityManager.createQuery(cq).getSingleResult();

        // construct and return the result
        return new HashMap<String, Long>() {
            {
                put(COUNT_TOTAL, totalRecords);
                put(COUNT_FILTERED, recordsFiltered);
            }
        };
    }

    private Predicate constructLimitPredicete(QueryContext context, final int rowsetLimit) {
        Predicate predicate = context.criteriaBuilder
                .lessThanOrEqualTo(context.from.get("id"), rowsetLimit);
//
//
//
//        Predicate result =  context.criteriaBuilder.and(context.criteriaBuilder.lessThanOrEqualTo(context.from.get(
//                "id"), rowsetLimit));
        return  predicate;
    }

    private Predicate constructWherePredicate(QueryContext context, final DataTableQueryParameters parameters) {
        // where predicates
        if(null != parameters.getSearchValue() && !parameters.getSearchValue().isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            parameters.getColumnInfos().stream().filter(ci -> ci.getSearchable()).forEach(c -> {
                String columnName = c.getData(); /* this holds column name for now, TODO: fix how the column name is stored in data table query parameters */
                // Converting all to string, consider to use a datatype mapping in the future
                Predicate predicate = context.criteriaBuilder
                        .like(context.criteriaBuilder.lower(
                                context.from.get(columnName).as(String.class)), parameters.getSearchValue().toLowerCase());
                // % must be passed explicitly, to this is commented out: String.format("%%%s%%", parameters.getSearchValue()));
                predicates.add(predicate);
            });
            return context.criteriaBuilder
                    .or(predicates.toArray(new Predicate[predicates.size()]));
        }
        return null;
    }

    private List<Order> constructOrderList(QueryContext context, final DataTableQueryParameters parameters) {
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
                    orderList.add(context.criteriaBuilder.asc(context.from.get(columnName.get())));
                else
                    orderList.add(context.criteriaBuilder.desc(context.from.get(columnName.get())));
        });
        return orderList;
    }

    private class DataTableQueryResult {
        // number of all records in the queried table: select * from the table
        private final Long  recordsTotal;

        // number or records that would be returned after applying the where predicates without the top for paging
        private final Long recordFiltered;

        // result set to be returned to client; after applying where and top for paging, so it's just one page of data
        private final List<T> filteredResult;

        public DataTableQueryResult(Long recordsTotal, Long recordFiltered, List<T> filteredResult) {
            this.recordsTotal = recordsTotal;
            this.recordFiltered = recordFiltered;
            this.filteredResult = filteredResult;
        }
    }
}
