package com.example.bootstraptabledemo.datatable.params;

import com.example.bootstraptabledemo.datatable.ColumnOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ColumnOrderImpl implements ColumnOrder {
    private Integer index;
    private Integer column;
    private String dir;
}
