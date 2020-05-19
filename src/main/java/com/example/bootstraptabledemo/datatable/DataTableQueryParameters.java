package com.example.bootstraptabledemo.datatable;

import java.util.Set;

public interface DataTableQueryParameters {
    String get_();

    Integer getDraw();

    Integer getStart();

    Integer getLength();

    String getSearchValue();

    Boolean getSearchRegex();

    Set<ColumnOrder> getColumnOrders();

    Set<ColumnInfo> getColumnInfos();
}
