package com.example.bootstraptabledemo.datatable.params;

import lombok.Builder;

public class SearchParam extends Param {

    @Builder(builderMethodName = "searchParamBuilder")
    public SearchParam(String id, String name, String value) {
        super(id, name, value);
    }
}
