package com.example.bootstraptabledemo.services;

import com.example.bootstraptabledemo.datatable.ColumnInfo;
import com.example.bootstraptabledemo.datatable.ColumnOrder;
import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PersonServiceImplTestIT {

    @Autowired
    PersonService personService;

    @Test
    void query() {
        DataTableQueryParameters params = createDataTableQueryParamters();
        DataTableResponse dataTableResponse = personService.query(params);
        assertNotNull(dataTableResponse);
        assertEquals(10, dataTableResponse.getData().size());
    }

    private DataTableQueryParameters createDataTableQueryParamters() {
        return new DataTableQueryParameters() {
            @Override
            public String get_() {
                return null;
            }

            @Override
            public Integer getDraw() {
                return 10;
            }

            @Override
            public Integer getStart() {
                return null;
            }

            @Override
            public Integer getLength() {
                return null;
            }

            @Override
            public String getSearchValue() {
                return null;
            }

            @Override
            public Boolean getSearchRegex() {
                return null;
            }

            @Override
            public Set<ColumnOrder> getColumnOrders() {
                return null;
            }

            @Override
            public Set<ColumnInfo> getColumnInfos() {
                return null;
            }
        };
    }
}