package com.acrel.utils;

import com.acrel.entity.base.ReflectObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Map;

public class MongoUtils {

    /**
     * 将查询条件对象转换为update
     * @return
     */
    public static <T> Update objectToUpdate(T t) {
        ReflectObject object = new ReflectObject(t);
        String[] props = object.getProps();
        Update update = new Update();
        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            Object value = null;
            try {
                value = object.getMethodValue(prop);
            } catch (Exception e) {
            } finally {
                update.set(prop, value);
            }
        }
        return update;
    }

    /**
     * 将查询条件对象转换为query
     * @param t
     */
    public static <T> Query objectToQuery(T t) {
        ReflectObject object = new ReflectObject(t);
        String[] props = object.getProps();
        Criteria criteria = new Criteria();
        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            Object value = null;
            try {
                value = object.getMethodValue(prop);
            } catch (Exception e) {
            } finally {
                criteria.and(prop).is(value);
            }
        }
        Query query = new Query();
        query.addCriteria(criteria);
        return query;
    }

    /**
     * 将查询条件Map对象转换为query
     * @param map
     */
    public static Query objectToQuery(Map<String, Object> map) {
        Criteria criteria = new Criteria();
        for (Map.Entry entry : map.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            criteria.and(key).is(value);
        }
        Query query = new Query();
        query.addCriteria(criteria);
        return query;
    }
}
