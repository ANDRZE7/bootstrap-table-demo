package com.example.bootstraptabledemo.datatable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
public class DataTableResponseBuilder implements DataTableResponse {
    private static final long serialVersionUID = 1L;

    private int recordsTotal;
    private int recordsFiltered;
    private int draw;

    @Singular
    private final List data;

    @Builder
    public DataTableResponseBuilder(int recordsTotal, int recordsFiltered, int draw, List data) {
        super();
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.draw = draw;
        this.data = data;
    }
}
