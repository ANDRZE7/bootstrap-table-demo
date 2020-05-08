package com.example.bootstraptabledemo.datatable.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ColumnParamImpl extends ParamImpl implements ColumnParam {
    private int columnId;

    @Builder(builderMethodName = "columnParamBuilder")
    public ColumnParamImpl(String id, String name, String value, ParamDataType dataType, int columnId) {
        super(id, name, value, dataType);
        this.columnId = columnId;
    }
}
