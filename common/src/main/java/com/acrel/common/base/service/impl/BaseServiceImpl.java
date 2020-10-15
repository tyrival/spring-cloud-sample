package com.acrel.common.base.service.impl;

import com.acrel.common.base.dao.AbstractBatchDao;
import com.acrel.common.base.service.BaseService;
import com.acrel.entity.base.*;
import com.acrel.common.base.dao.BaseDAO;
import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

/**
 * @Author: ZhouChenyu
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    private static final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    public abstract BaseDAO<T> getDAO();

    @Override
    public T create(T t) {
        try {
            this.beforeCreate(t);
            int i = this.getDAO().insert(t);
            this.afterCreate(t);
            return i > 0 ? t : null;
        } catch (RuntimeException e) {
            log.error("保存时异常，{}", e.getMessage());
            throw new CommonException(ExceptionEnum.SAVE_FAIL);
        }
    }

    @Override
    public T update(T t) {
        try {
            this.beforeUpdate(t);
            int i = this.getDAO().update(t);
            this.afterUpdate(t);
            return i > 0 ? t : null;
        } catch (RuntimeException e) {
            log.error("更新时异常，{}", e.getMessage());
            throw new CommonException(ExceptionEnum.UPDATE_FAIL);
        }

    }

    @Override
    public int delete(Integer id) {
        try {
            T t = this.get(id);
            this.beforeDelete(t);
            int i = this.getDAO().delete(id);
            this.afterDelete(t);
            return i;
        } catch (RuntimeException e) {
            log.error("删除时异常，{}", e.getMessage());
            throw new CommonException(ExceptionEnum.DELETE_FAIL);
        }
    }

    @Override
    public T get(Integer id) {
        try {
            return this.getDAO().get(id);
        } catch (RuntimeException e) {
            log.error("根据ID查询时异常，{}", e.getMessage());
            throw new CommonException(ExceptionEnum.QUERY_FAIL);
        }

    }

    @Override
    public List list() {
        try {
            return this.getDAO().list(null);
        } catch (RuntimeException e) {
            log.error("获取列表时异常，{}", e.getMessage());
            throw new CommonException(ExceptionEnum.QUERY_FAIL);
        }
    }

    @Override
    public List list(T params) {
        try {
            return this.getDAO().list(params);
        } catch (RuntimeException e) {
            log.error("获取列表时异常，{}", e.getMessage());
            throw new CommonException(ExceptionEnum.QUERY_FAIL);
        }
    }

    @Override
    public Result listByPage(PageQuery pageQuery) {
        return listByPage(pageQuery, (object) -> (() -> this.getDAO().listByPage(object)));
    }

    @Override
    public Result listByPage(PageQuery pageQuery, Function<Object, ISelect> handler) {
        try {
            PageInfo pageInfo = PageHelper.startPage(pageQuery.getPageIndex(), pageQuery.getPageSize())
                    .doSelectPageInfo(handler.apply(pageQuery.getParams()));
            return new Result<>(pageInfo.getList(), getPage(pageInfo));
        } catch (RuntimeException e) {
            log.error("分页获取列表时异常，{}", e.getMessage());
            throw new CommonException(ExceptionEnum.QUERY_FAIL);
        }
    }

    @Override
    public void deleteByBatch(List<Object> idList, String statement) {
        new AbstractBatchDao<Object>(idList) {
            @Override
            protected void doExecute(SqlSession sqlSession, Object value) throws RuntimeException {
                sqlSession.delete(statement, value);
            }
        }.execute();
    }

    protected Page getPage(PageInfo pageInfo) {
        Page page = new Page();
        page.setPageIndex(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());
        return page;
    }
}
