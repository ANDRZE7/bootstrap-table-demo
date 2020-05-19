package com.example.bootstraptabledemo.datatable.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
class ParamImpl implements Param {
    private final String id;
    private final String name;
    private final String value;
    private final ParamDataType dataType;
}
