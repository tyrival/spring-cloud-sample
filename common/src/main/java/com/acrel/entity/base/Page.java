package com.acrel.entity.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 分页类
 * @Author: ZhouChenyu
 */
@Data
public class Page implements Serializable {

    /** 默认每页显示记录数 */
    private static final int DEFAULT_PAGE_SIZE = 15;

    /** 默认页码 */
    private static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * 当前页数，默认第一页
     */
    protected int pageIndex ;

    /**
     * 每页记录数
     */
    protected int pageSize;

    /**
     * 总记录数
     */
    protected long totalCount;

    /**
     * 总页数
     */
    protected int pageCount;

    public Page() {
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.pageIndex = DEFAULT_PAGE_INDEX;
        this.pageCount = 1;
        this.totalCount = 0;
    }

    public int getPageIndex() {
        return pageIndex == 0 ? DEFAULT_PAGE_INDEX : pageIndex;
    }

    public int getPageSize() {
        return pageSize == 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        this.setPageCount();
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        this.setPageCount();
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        this.setPageCount();
    }

    private void setPageCount() {
        if (this.totalCount == 0) {
            return;
        }
        if (this.pageSize == 0) {
            this.pageSize = 10;
        }
        this.pageCount = (int) Math.ceil(this.totalCount * 1.0 / this.pageSize);
        if (this.pageCount == 0) {
            this.pageCount = 1;
        }
        if (this.pageCount < this.pageIndex) {
            this.pageIndex = 1;
        }
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder().
                append("Page { pageIndex=").append(pageIndex).
                append(", pageSize=").append(pageSize)
                .append(", totalCount=").append(totalCount)
                .append(", pageCount=").append(pageCount)
                .append(" }");
        return builder.toString();
    }
}
