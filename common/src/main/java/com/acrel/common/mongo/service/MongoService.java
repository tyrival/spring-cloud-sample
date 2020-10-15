package com.acrel.common.mongo.service;

import com.acrel.entity.base.Page;
import com.acrel.entity.base.PageQuery;
import com.acrel.entity.base.Result;
import com.acrel.exceptions.CommonException;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @Author: ZhouChenyu
 */
public interface MongoService<T> {

    /**
     * 保存一个对象到mongodb
     * @param t
     */
    T save(T t) ;

    /**
     * 修改匹配到的第一条记录
     * @param srcObj
     * @param targetObj
     */
    long updateFirst(T srcObj, T targetObj);

    /***
     * 修改匹配到的所有记录
     * @param srcObj
     * @param targetObj
     */
    long updateMulti(T srcObj, T targetObj);

    /***
     * 修改匹配到的记录，若不存在该记录则进行添加
     * @param srcObj
     * @param targetObj
     */
    long updateInsert(T srcObj, T targetObj);

    /**
     * 删除数据
     */
    long delete(T t);

    /**
     * 根据id删除数据
     */
    long deleteById(Integer id);

    /**
     * 通过一定的条件查询一个实体
     * @param t
     */
    T findOne(T t) ;

    /**
     * 通过条件查询实体(集合)
     * @param t
     */
    List<T> find(T t) ;

    /**
     * 通过ID获取记录
     * @param id
     */
    T findById(Integer id) ;

    /**
     * 分页查询
     * @param page
     * @param t
     * @return
     */
    Result findByPage(PageQuery<T> pageQuery);

    /**
     * 求数据总和
     * @param t
     * @return
     */
    long count(T t);

    default <K> void beforeSave(K k) throws CommonException {}
    default <K> void afterSave(K k) throws CommonException {}
    default <K> void beforeUpdate(K srgObj, K tarObj) throws CommonException {}
    default <K> void afterUpdate(K srgObj, K tarObj) throws CommonException {}
    default <K> void beforeDelete(K k) throws CommonException {}
    default <K> void afterDelete(K k) throws CommonException {}
}
