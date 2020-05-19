package com.example.bootstraptabledemo.datatable.params;

import lombok.Builder;

class SearchParamImpl extends ParamImpl implements SearchParam {

    @Builder(builderMethodName = "searchParamBuilder")
    public SearchParamImpl(String id, String name, String value, ParamDataType dataType) {
        super(id, name, value, dataType);
    }
}
