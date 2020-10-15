package com.acrel.common.mongo.service.impl;

import com.acrel.common.mongo.service.MongoService;
import com.acrel.entity.base.Page;
import com.acrel.entity.base.PageQuery;
import com.acrel.entity.base.Result;
import com.acrel.utils.MongoUtils;
import com.acrel.utils.ReflectionUtils;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @Author: ZhouChenyu
 */
public abstract class MongoServiceImpl<T> implements MongoService<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public T save(T t) {
        this.beforeSave(t);
        t = mongoTemplate.save(t);
        this.afterSave(t);
        return t;
    }

    @Override
    public long updateFirst(T srcObj, T targetObj) {
        Query query = MongoUtils.objectToQuery(srcObj);
        Update update = MongoUtils.objectToUpdate(targetObj);
        this.beforeUpdate(srcObj, targetObj);
        UpdateResult result = this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
        this.afterUpdate(srcObj, targetObj);
        return result.getModifiedCount();
    }

    @Override
    public long updateMulti(T srcObj, T targetObj) {
        Query query = MongoUtils.objectToQuery(srcObj);
        Update update = MongoUtils.objectToUpdate(targetObj);
        this.beforeUpdate(srcObj, targetObj);
        UpdateResult result = this.mongoTemplate.updateMulti(query, update, this.getEntityClass());
        this.afterUpdate(srcObj, targetObj);
        return result.getModifiedCount();
    }

    @Override
    public long updateInsert(T srcObj, T targetObj) {
        Query query = MongoUtils.objectToQuery(srcObj);
        Update update = MongoUtils.objectToUpdate(targetObj);
        this.beforeUpdate(srcObj, targetObj);
        UpdateResult result = this.mongoTemplate.upsert(query, update, this.getEntityClass());
        this.afterUpdate(srcObj, targetObj);
        return result.getModifiedCount();
    }

    @Override
    public long delete(T t) {
        this.beforeDelete(t);
        DeleteResult result = mongoTemplate.remove(t);
        this.afterDelete(t);
        return result.getDeletedCount();
    }

    @Override
    public long deleteById(Integer id) {
        Criteria criteria = Criteria.where("_id").is(id);
        if (null != criteria) {
            Query query = new Query(criteria);
            T obj = this.mongoTemplate.findOne(query, this.getEntityClass());
            if (obj != null) {
                return this.delete(obj);
            }
        }
        return 0;
    }

    @Override
    public List<T> find(T t) {
        Query query = MongoUtils.objectToQuery(t);
        return mongoTemplate.find(query, this.getEntityClass());
    }

    @Override
    public T findOne(T t) {
        Query query = MongoUtils.objectToQuery(t);
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    @Override
    public T findById(Integer id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    @Override
    public Result findByPage(PageQuery<T> pageQuery) {

        Page page = new Page();
        int pageIndex = page.getPageIndex();
        int pageSize = page.getPageSize();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        T t = pageQuery.getParams();
        long count = this.count(t);
        page.setTotalCount(count);

        Query query = MongoUtils.objectToQuery(t);
        query.skip((pageIndex - 1) * pageSize).limit(pageSize);
        query.limit(pageSize);
        List data = this.mongoTemplate.find(query, this.getEntityClass());
        return new Result(data, page);
    }

    @Override
    public long count(T t) {
        Query query = MongoUtils.objectToQuery(t);
        return mongoTemplate.count(query, this.getEntityClass());
    }


    /**
     * 获取需要操作的实体类class
     *
     * @return
     */
    private Class<T> getEntityClass() {
        return ReflectionUtils.getSuperClassGenericType(this.getClass());
    }

}
