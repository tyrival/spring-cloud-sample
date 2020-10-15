package com.acrel.entity.base;

import lombok.Data;

/**
 * @Description 分页查询条件类
 * @author ZhouChenyu
 **/
@Data
public class PageQuery<T> {

    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 每页行数
     */
    private Integer pageSize;

    /**
     * 查询参数
     */
    private T params;

    public PageQuery() {
    }

    public PageQuery(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "当前页数：" + this.pageIndex + "，每页显示记录数：" + this.pageSize;
    }
}
