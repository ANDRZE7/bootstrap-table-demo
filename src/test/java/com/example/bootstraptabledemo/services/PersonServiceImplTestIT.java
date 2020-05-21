package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.datatable.params.DataTableQueryParametersLocalFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PersonServiceImplTestIT {

    @Autowired
    PersonService personService;

    @Test
    void query() {
        DataTableQueryParameters params = createDataTableQueryParamters();
        DataTableResponse dataTableResponse = personService.query(params, 1000);
        assertNotNull(dataTableResponse);
        assertEquals(10, dataTableResponse.getData().size());
    }

    private DataTableQueryParameters createDataTableQueryParamters() {
        return DataTableQueryParametersLocalFactory.create();
    }
}