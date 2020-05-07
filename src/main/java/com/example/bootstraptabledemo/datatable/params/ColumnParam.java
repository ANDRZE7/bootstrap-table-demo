package com.example.bootstraptabledemo.datatable.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnParam extends Param {
    private int columnId;

    @Builder(builderMethodName = "columnParamBuilder")
    public ColumnParam(String id, String name, String value, int columnId) {
        super(id, name, value);
        this.columnId = columnId;
    }
}
