package com.example.bootstraptabledemo.datatable.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderParam extends Param {
    private int index;

    @Builder(builderMethodName = "orderParamBuilder")
    public OrderParam(String id, String name, String value, int index) {
        super(id, name, value);
        this.index = index;
    }
}
