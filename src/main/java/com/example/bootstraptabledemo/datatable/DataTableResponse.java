package com.example.bootstraptabledemo.datatable;

public interface DataTableResponse {
    int getRecordsTotal();

    int getRecordsFiltered();

    int getDraw();

    java.util.List getData();
}
