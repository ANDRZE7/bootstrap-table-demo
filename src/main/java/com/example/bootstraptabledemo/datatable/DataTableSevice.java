package com.example.bootstraptabledemo.datatable;

public interface DataTableSevice {

    DataTableResponse query(DataTableQueryParameters query, int limit);

}
