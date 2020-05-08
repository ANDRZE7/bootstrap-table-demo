package com.example.bootstraptabledemo.datatable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnOrderImpl implements ColumnOrder {
    private Integer index;
    private Integer column;
    private String dir;
}
