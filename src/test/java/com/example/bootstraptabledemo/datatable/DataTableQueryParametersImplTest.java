package com.example.bootstraptabledemo.datatable;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTableQueryParametersImplTest {

    @Test
    public void builderTest() {
        // DataTableQueryParameters dtqp = new DataTableQueryParameters();

        // DataTableQueryParameters.builder().
    }

    @Test
    public void buildeHasNoDefaultParameterlessConstructor() {
        assertEquals(0,
                Arrays.stream(DataTableQueryParametersImpl.class.getConstructors())
                        .filter(c -> c.getParameterCount() == 0).count());
    }

}