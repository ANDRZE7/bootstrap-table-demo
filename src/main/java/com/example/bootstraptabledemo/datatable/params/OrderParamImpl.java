package com.example.bootstraptabledemo.datatable.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class OrderParamImpl extends ParamImpl implements OrderParam {
    private int index;

    @Builder(builderMethodName = "orderParamBuilder")
    public OrderParamImpl(String id, String name, String value, ParamDataType dataType, int index) {
        super(id, name, value, dataType);
        this.index = index;
    }
}
