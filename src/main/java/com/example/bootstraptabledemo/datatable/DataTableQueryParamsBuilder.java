package com.example.bootstraptabledemo.datatable;

import com.example.bootstraptabledemo.datatable.params.ColumnParam;
import com.example.bootstraptabledemo.datatable.params.OrderParam;
import com.example.bootstraptabledemo.datatable.params.Param;
import com.example.bootstraptabledemo.datatable.params.SearchParam;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
class DataTableQueryParamsBuilder {

    private DataTableQueryParamsBuilder() {}

    public static DataTableQueryParamsBuilder builder() {
        return new DataTableQueryParamsBuilder();
    }

    private String _;
    private Integer draw;
    private Integer length;
    private Integer start;
    private String searchValue;
    private Boolean searchRegex;
    private final Set<ColumnOrder> columnOrders = new HashSet<>();
    private final Set<ColumnInfo> columnInfos = new HashSet<>();

    public DataTableQueryParamsBuilder setParams(List<Param> params) {
        params.forEach( p-> this.mergeParam(p));
        return this;
    }

    private void mergeParam(Param param) {
        if(param instanceof ColumnParam) mergeColumnParam((ColumnParam) param);
        else
        if(param instanceof SearchParam) mergeSearchParam((SearchParam) param);
        else
        if(param instanceof OrderParam) mergeOrderParam((OrderParam) param);
        else
            mergeGeneralParam(param);
    }

    private void mergeGeneralParam(Param param) {
        setObjectValueFromParam(this, param);
    }

    private void mergeOrderParam(OrderParam param) {
        ColumnOrder order = columnOrders.stream().filter(o -> param.getIndex()==o.getIndex())
                .findFirst().orElse(new ColumnOrder());
        order.setIndex(param.getIndex());
        setObjectValueFromParam(order, param);
        columnOrders.add(order);
    }

    private void mergeSearchParam(SearchParam param) {
        setObjectValueFromParam(this, param);
    }

    private void mergeColumnParam(ColumnParam param) {
        ColumnInfo info = columnInfos.stream().filter(i -> param.getColumnId()==i.getIndex())
                .findFirst().orElse(new ColumnInfo());
        info.setIndex(param.getColumnId());

        // use reflection to set column info value
        setObjectValueFromParam(info, param);
        columnInfos.add(info);
    }

    public DataTableQueryParameters build() {
        return DataTableQueryParametersImpl.builder()
                    ._(_)
                    .draw(draw)
                    .length(length)
                    .start(start)
                    .searchValue(searchValue)
                    .searchRegex(searchRegex)
                    .columnInfos(columnInfos)
                    .columnOrders(columnOrders)
                    .build();
    }

    private void setObjectValueFromParam(Object object, Param param) {
        try {
            Field field = object.getClass().getDeclaredField(param.getName());
            boolean access = field.isAccessible();
            field.setAccessible(true);
            field.set(object, field.getType().getConstructor(String.class).newInstance(param.getValue()));
            field.setAccessible(access);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
