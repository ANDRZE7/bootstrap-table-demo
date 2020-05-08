package com.example.bootstraptabledemo.datatable;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void hashSetTest() {
        Set<String> set = new HashSet<String>(){
            {
                add("a");
                add("b");
                add("c");
            }
        };

        Set<String> bset = new HashSet<>(set);

    }


}