package com.example.bootstraptabledemo.datatable;

public interface DataTableResponse {
    Long getRecordsTotal();

    Long getRecordsFiltered();

    int getDraw();

    java.util.List getData();
}
