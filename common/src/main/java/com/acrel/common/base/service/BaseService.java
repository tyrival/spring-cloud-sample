package com.acrel.common.base.service;

import com.acrel.entity.base.PageQuery;
import com.acrel.entity.base.Result;
import com.acrel.exceptions.CommonException;
import com.github.pagehelper.ISelect;

import java.util.List;
import java.util.function.Function;

/**
 * @Author: ZhouChenyu
 */
public interface BaseService<T> {

    T create(T t);

    T update(T t);

    int delete(Integer id);

    T get(Integer id);

    List<T> list();

    /**
     * 获取列表
     * @param params   查询参数对象
     * @return 查询结果
     **/
    List list(T params);

    /**
     * 分页查询
     * @param params            查询参数
     * @return com.acrel.entity.base.Result
     **/
    Result listByPage(PageQuery params);

    /**
     * 分页查询
     * @param params            查询参数
     * @param handler           自定义的查询处理逻辑
     * @return com.acrel.entity.base.Result
     **/
    Result listByPage(PageQuery params, Function<Object, ISelect> handler);

    /**
     * 批量删除
     * @param valueList     删除的记录列表
     * @param statement     删除sql的ID
     **/
    void deleteByBatch(List<Object> valueList, String statement);

    default <K> void beforeCreate(K k) throws CommonException {}
    default <K> void afterCreate(K k) throws CommonException {}
    default <K> void beforeUpdate(K k) throws CommonException {}
    default <K> void afterUpdate(K k) throws CommonException {}
    default <K> void beforeDelete(K k) throws CommonException {}
    default <K> void afterDelete(K k) throws CommonException {}
}
