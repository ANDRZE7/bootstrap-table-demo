package com.example.bootstraptabledemo.datatable.params;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;

public class DataTableQueryParametersLocalFactory {

    public static DataTableQueryParameters create() {
        return DataTableQueryParametersImpl.builder().build();
    }
}