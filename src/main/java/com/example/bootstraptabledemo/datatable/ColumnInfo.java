package com.example.bootstraptabledemo.datatable;

public interface ColumnInfo {
    Integer getIndex();

    String getData();

    String getName();

    Boolean getSearchable();

    Boolean getOrderable();

    String getSearchValue();

    Boolean getSearchRegex();
}
