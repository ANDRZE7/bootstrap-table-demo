package com.example.bootstraptabledemo.datatable;

import com.example.bootstraptabledemo.datatable.params.DataTableQueryParametersFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataTableQueryParametersFactoryTest {

    // test data
    Map<String, String> map = new HashMap<String, String>() {
        {
            put("draw", "1");
            put("columns[0][data]", "id");
            put("columns[0][name]", "");
            put("columns[0][searchable]", "true");
            put("columns[0][orderable]", "true");
            put("columns[0][search][value]", "");
            put("columns[0][search][regex]", "false");
            put("columns[1][data]", "name");
            put("columns[1][name]", "");
            put("columns[1][searchable]", "true");
            put("columns[1][orderable]", "true");
            put("columns[1][search][value]", "");
            put("columns[1][search][regex]", "false");
            put("columns[2][data]", "surname");
            put("columns[2][name]", "");
            put("columns[2][searchable]", "true");
            put("columns[2][orderable]", "true");
            put("columns[2][search][value]", "");
            put("columns[2][search][regex]", "false");
            put("columns[3][data]", "email");
            put("columns[3][name]", "");
            put("columns[3][searchable]", "true");
            put("columns[3][orderable]", "true");
            put("columns[3][search][value]", "");
            put("columns[3][search][regex]", "false");
            put("columns[4][data]", "dateOfBirth");
            put("columns[4][name]", "");
            put("columns[4][searchable]", "true");
            put("columns[4][orderable]", "true");
            put("columns[4][search][value]", "");
            put("columns[4][search][regex]", "false");
            put("columns[5][data]", "personalNumber");
            put("columns[5][name]", "");
            put("columns[5][searchable]", "true");
            put("columns[5][orderable]", "true");
            put("columns[5][search][value]", "");
            put("columns[5][search][regex]", "false");
            put("order[0][column]", "0");
            put("order[0][dir]", "asc");
            put("start", "0");
            put("length", "10");
            put("search[value]", "");
            put("search[regex]", "false");
            put("order[1][column]", "1");
            put("order[1][dir]", "desc");
            put("_", "1588776055582");
        }
    };

    @Test
    void processMapTest() throws ParamResolverException {
        // when
        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // then expecting six columns and two orders
        assertEquals(6, params.getColumnInfos().size());
        assertEquals(2, params.getColumnOrders().size());
    }

    @Test
    void processOrderTest() throws ParamResolverException {
        // when
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("order[0][column]", "0");
                put("order[0][dir]", "asc");
            }
        };
        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // then
        Set<ColumnOrder> cos = params.getColumnOrders();
        assertEquals(1, cos.size());
        ColumnOrder co = cos.stream().findFirst().orElse(null);
        assertNotNull(co);
        assertEquals(0, co.getColumn());
        assertEquals("asc", co.getDir());
    }


    @Test
    void processUnderscoreParamTest() throws ParamResolverException {

        // for given map
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("_", "1588776055582");
            }
        };

        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // expecting
        assertEquals("1588776055582", params.get_());
    }

    @Test
    void searchParamsTest() throws ParamResolverException {
        final String value = "093RY32-9Nf23";
        // when
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("search[value]", value);
                put("search[regex]", "false");
            }
        };
        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // then
        assertEquals(value, params.getSearchValue());
        assertEquals(false, params.getSearchRegex());
    }

    @Test
    void searchParamsEmptyStringTrueRegexTest() throws ParamResolverException {
        final String value = "oJG9-w4-g9WEon";
        // when
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("search[value]", value);
                put("search[regex]", "true");
            }
        };
        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // then
        assertEquals(value, params.getSearchValue());
        assertEquals(true, params.getSearchRegex());
    }

    @Test
    void processColumnTest() throws ParamResolverException {
        // when
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("columns[0][data]", "id");
                put("columns[0][name]", "kjgy8474");
                put("columns[0][searchable]", "true");
                put("columns[0][orderable]", "true");
                put("columns[0][search][value]", "0akai38j");
                put("columns[0][search][regex]", "false");
            }
        };
        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // then
        Set<ColumnInfo> cis = params.getColumnInfos();
        assertEquals(1, cis.size());
        ColumnInfo ci = cis.stream().findFirst().orElse(null);
        assertNotNull(ci);
        assertEquals("id", ci.getData());
        assertEquals("kjgy8474", ci.getName());
        assertEquals(true, ci.getSearchable());
        assertEquals(true, ci.getOrderable());
        assertEquals("0akai38j", ci.getSearchValue());
        assertEquals(false, ci.getSearchRegex());
    }

    @Test
    void processSimpleProps() throws ParamResolverException {
        // when
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("draw", "1");
                put("start", "0");
                put("length", "10");
                put("search[value]", "oNWG)408g3");
                put("search[regex]", "false");
                put("_", "152");
            }
        };
        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // then
        assertEquals(1, params.getDraw());
        assertEquals(0, params.getStart());
        assertEquals(10, params.getLength());
        assertEquals("oNWG)408g3", params.getSearchValue());
        assertEquals(false, params.getSearchRegex());
        assertEquals("152", params.get_());
    }

    @Test
    void selectingSearchableOrderableColumns() throws ParamResolverException {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("draw", "1");
                put("columns[0][data]", "id");
                put("columns[0][name]", "");
                put("columns[0][searchable]", "true");
                put("columns[0][orderable]", "true");
                put("columns[0][search][value]", "");
                put("columns[0][search][regex]", "false");
                put("columns[1][data]", "name");
                put("columns[1][name]", "");
                put("columns[1][searchable]", "false");
                put("columns[1][orderable]", "false");
                put("columns[1][search][value]", "");
                put("columns[1][search][regex]", "false");
                put("columns[2][data]", "surname");
                put("columns[2][name]", "");
                put("columns[2][searchable]", "false");
                put("columns[2][orderable]", "true");
                put("columns[2][search][value]", "");
                put("columns[2][search][regex]", "false");
            }
        };

        DataTableQueryParameters params = DataTableQueryParametersFactory.createFromParametersMap(map);

        // then
        List<ColumnInfo> searchableColumns = params.getColumnInfos().stream().filter(ci -> ci.getSearchable()).collect(Collectors.toList());
        List<ColumnInfo> orderableColumns = params.getColumnInfos().stream().filter(ci -> ci.getOrderable()).collect(Collectors.toList());
        assertEquals(1, searchableColumns.size());
        assertEquals(2, orderableColumns.size());

        // output column data
        params.getColumnInfos().stream().filter(ci -> ci.getSearchable()).forEach(
                c -> System.out.println(c.getData())
        );
    }

}