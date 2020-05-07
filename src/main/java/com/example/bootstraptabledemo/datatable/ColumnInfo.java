package com.example.bootstraptabledemo.datatable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnInfo {
    private Integer index;
    private String data;
    private String name;
    private Boolean searchable;
    private Boolean orderable;
    private String searchValue;
    private Boolean searchRegex;
}
