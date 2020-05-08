package com.example.bootstraptabledemo.datatable;

import com.example.bootstraptabledemo.datatable.params.ColumnParamImpl;
import com.example.bootstraptabledemo.datatable.params.OrderParamImpl;
import com.example.bootstraptabledemo.datatable.params.Param;
import com.example.bootstraptabledemo.datatable.params.SearchParamImpl;
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
    private final Set<ColumnOrderImpl> columnOrders = new HashSet<>();
    private final Set<ColumnInfoImpl> columnInfos = new HashSet<>();

    public DataTableQueryParamsBuilder setParams(List<Param> params) {
        params.forEach(p-> this.mergeParam(p));
        return this;
    }

    private void mergeParam(Param param) {
        if(param instanceof ColumnParamImpl) mergeColumnParam((ColumnParamImpl) param);
        else
        if(param instanceof SearchParamImpl) mergeSearchParam((SearchParamImpl) param);
        else
        if(param instanceof OrderParamImpl) mergeOrderParam((OrderParamImpl) param);
        else
            mergeGeneralParam(param);
    }

    private void mergeGeneralParam(Param param) {
        setObjectValueFromParam(this, param);
    }

    private void mergeOrderParam(OrderParamImpl param) {
        ColumnOrderImpl order = columnOrders.stream().filter(o -> param.getIndex()==o.getIndex())
                .findFirst().orElse(new ColumnOrderImpl());
        order.setIndex(param.getIndex());
        setObjectValueFromParam(order, param);
        columnOrders.add(order);
    }

    private void mergeSearchParam(SearchParamImpl param) {
        setObjectValueFromParam(this, param);
    }

    private void mergeColumnParam(ColumnParamImpl param) {
        ColumnInfoImpl info = columnInfos.stream().filter(i -> param.getColumnId()==i.getIndex())
                .findFirst().orElse(new ColumnInfoImpl());
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
                    .columnInfos(new HashSet<ColumnInfo>(columnInfos))
                    .columnOrders(new HashSet<ColumnOrder>(columnOrders))
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
