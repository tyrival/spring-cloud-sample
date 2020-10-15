package com.acrel.common.base.dao;

import java.util.List;

/**
 * @Author: ZhouChenyu
 */
public interface BaseDAO<T> {

    /**
     * 创建
     */
    int insert(T t);

    /**
     * 修改
     */
    int update(T t);

    /**
     * 删除
     */
    int delete(Integer id);

    /**
     * 根据ID查询
     */
    T get(Integer id);

    /**
     * 分页查询
     * @param params 查询参数
     * @return 查询结果
     **/
    List<T> listByPage(Object params);

    /**
     * 获取列表
     * @param params  查询参数Map
     **/
    List<T> list(T params);

}
