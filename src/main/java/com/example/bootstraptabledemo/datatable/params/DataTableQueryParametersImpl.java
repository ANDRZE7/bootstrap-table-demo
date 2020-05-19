package com.example.bootstraptabledemo.datatable.params;

import com.example.bootstraptabledemo.datatable.ColumnInfo;
import com.example.bootstraptabledemo.datatable.ColumnOrder;
import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
class DataTableQueryParametersImpl implements Serializable, DataTableQueryParameters {
    private static final long serialVersionUID = 1L;

    private final String _;
    private final Integer draw;
    private final Integer start;
    private final Integer length;
    private final String searchValue;
    private final Boolean searchRegex;

    private final Set<ColumnOrder> columnOrders;
    private final Set<ColumnInfo> columnInfos;

    @Builder
    public DataTableQueryParametersImpl(String _, Integer draw, Integer start, Integer length, String searchValue, Boolean searchRegex, Set<ColumnOrder> columnOrders, Set<ColumnInfo> columnInfos) {
        this._ = null == _ ? "" : _;
        this.draw = null == draw ? 0 : draw;
        this.start = null == start ? 0 : start;
        this.length = null == length ? 10: length;
        this.searchValue = null == searchValue ? "" : searchValue;
        this.searchRegex = null == searchRegex ? false : searchRegex;
        this.columnOrders = columnOrders != null ? columnOrders : new HashSet<>();
        this.columnInfos = columnInfos != null ? columnInfos : new HashSet<>();
    }
}
