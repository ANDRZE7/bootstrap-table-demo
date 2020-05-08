package com.example.bootstraptabledemo.datatable.params;

import com.example.bootstraptabledemo.datatable.ColumnInfo;
import com.example.bootstraptabledemo.datatable.ColumnOrder;
import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Builder
@Getter
@Setter
class DataTableQueryParametersImpl implements Serializable, DataTableQueryParameters {
    private static final long serialVersionUID = 1L;

    private String _;
    private Integer draw;
    private Integer start;
    private Integer length;
    private String searchValue;
    private Boolean searchRegex;

    private final Set<ColumnOrder> columnOrders ;
    private final Set<ColumnInfo> columnInfos ;
}
