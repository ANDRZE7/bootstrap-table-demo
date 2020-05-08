package com.example.bootstraptabledemo.datatable;

import com.example.bootstraptabledemo.datatable.params.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DataTableQueryParametersFactory {

    private static final String[] SIMPLE_PARAMS_NAMES = {"draw", "start", "length", "_"};
    private static final String COLUMN_PARAM_PATTERN = "columns\\[\\d+\\].*";
    private static final String ORDER_PARAM_PATTERN = "order\\[\\d+\\]\\[(column|dir)\\]";
    private static final String SEARCH_PARAM_PATTERN = "search\\[(value|regex)\\]";

    private DataTableQueryParametersFactory() {}

    public static DataTableQueryParameters createFromParametersMap(Map<String, String> parametersMap) throws ParamResolverException {

        // parse and collect params
        List<Param> params = new ArrayList<>();
        List<ParamResolverException> exceptions = new ArrayList<>();
        parametersMap.entrySet().forEach( entry -> {
            try {
                params.add(resolveFromMapKey(entry));
            } catch (ParamResolverException exception) {
                exception.addSuppressed(exception);
            }
        });

        // if errors then log and throw
        if(!exceptions.isEmpty()) {
            exceptions.stream().forEach(e ->log.error(e.getMessage()));
            throw new ParamResolverException("Error while analysing DataTableQueryParameters from ParametersMap.");
        }

        // process params
        return DataTableQueryParamsBuilder.builder().setParams(params).build();
    }

    private static Param resolveFromMapKey(Map.Entry<String, String> entry) throws ParamResolverException {
        // if value found in SIMPLE_PARAMS_NAMES then resolve with resolveSimple
        if(!Arrays.stream(SIMPLE_PARAMS_NAMES).filter(f -> f.compareTo(entry.getKey()) == 0).findAny().orElse("").isEmpty())
            return resolveSimple(entry);

        // if matches to columns regular expression than resolve with columnResolver
        if(entry.getKey().matches(COLUMN_PARAM_PATTERN))
            return resolveWithColumnResolver(entry);

        // following matches

        if(entry.getKey().matches(SEARCH_PARAM_PATTERN))
            return resolveWithSearchResolver(entry);

        if(entry.getKey().matches(ORDER_PARAM_PATTERN))
            return resolveWithOrderResolver(entry);

        // throw exception if no resolver could be matched
        else
            throw new ParamResolverException(String.format("Parameter cannot be resolved from key: '%s'", entry.getKey()));
    }

    private static Param resolveWithOrderResolver(Map.Entry<String, String> entry) {
        try {
            int index = Integer.parseInt(entry.getKey().replaceAll("[^0-9]", ""));
            return OrderParamImpl.orderParamBuilder()
                    .id(entry.getKey())
                    .index(index)
                    .name(extractName(entry.getKey().replaceAll("order\\["+index+"\\]", "")))
                    .value(entry.getValue())
                    .build();
        } catch (NumberFormatException numberFormatException) {
            log.error("Error extracting column id from entry: {}.", entry);
            throw numberFormatException;
        }
    }

    private static Param resolveWithSearchResolver(Map.Entry<String, String> entry) {
        return SearchParamImpl.builder().id(entry.getKey())
                .name(extractName(entry.getKey().replaceAll("search", "[search]")))
                .value(entry.getValue()).build();
    }

    private static Param resolveWithColumnResolver(Map.Entry<String, String> entry) {
        try {
            int columnId = Integer.parseInt(entry.getKey().replaceAll("[^0-9]", ""));
            return ColumnParamImpl.columnParamBuilder()
                    .id(entry.getKey())
                    .columnId(columnId)
                    .name(extractName(entry.getKey().replaceAll("columns\\["+columnId+"\\]", "")))
                    .value(entry.getValue())
                    .build();
        } catch (NumberFormatException numberFormatException) {
            log.error("Error extracting column id from entry: {}.", entry);
            throw numberFormatException;
        }
    }

    private static Param resolveSimple(Map.Entry<String, String> entry) {
        return ParamImpl.builder().id(entry.getKey()).name(entry.getKey()).value(entry.getValue()).build();
    }

    /*
       When given "[elem][child][grangChild]"
           should return "elemChildGrandChild"
    */
    private static String extractName(String namesBracketsSurrounded) {
        // select text between square brackets inclusive
        final String REGEX = "\\[(.*?)\\]";

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(namesBracketsSurrounded);   // get a matcher object
        int count = 0;

        StringBuilder builder = new StringBuilder();
        while(matcher.find()) {
            count++;
            String matchedValue = namesBracketsSurrounded.substring(matcher.start()+1, matcher.end()-1);
            builder.append(count != 1 ? capitalizeFirstLetter(matchedValue) : matchedValue);
        }
        return builder.toString();
    }

    private static String capitalizeFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}