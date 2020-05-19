package com.example.bootstraptabledemo.datatable.params;

import com.example.bootstraptabledemo.datatable.ColumnInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ColumnInfoImpl implements ColumnInfo {
    private Integer index;
    private String data;
    private String name;
    private Boolean searchable;
    private Boolean orderable;
    private String searchValue;
    private Boolean searchRegex;
}
