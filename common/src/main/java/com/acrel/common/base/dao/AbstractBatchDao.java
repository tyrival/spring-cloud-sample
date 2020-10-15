package com.acrel.common.base.dao;

import com.acrel.utils.SpringContextUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ZhouChenyu
 **/     
public abstract class AbstractBatchDao<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBatchDao.class);

    /** 执行SQL的参数列表 <T> 参数的类型 */
    private List<T> paramList;

    /** 默认的批量提交的次数 */
    private static final Integer DEFAULT_BATCH_SIZE = 100;

    public AbstractBatchDao(List<T> paramList){
        this.paramList = paramList;
    }


    public void execute() throws RuntimeException {
        execute(DEFAULT_BATCH_SIZE);
    }

    public void execute(int batchSize) throws RuntimeException {
        if(batchSize < 1){
            batchSize = DEFAULT_BATCH_SIZE;
        }
        if (CollectionUtils.isEmpty(paramList)) {
            return;
        }

        SqlSessionFactory sqlSessionFactory = SpringContextUtils.getBean(SqlSessionFactory.class);
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int i = 0;
        try {
            int length = paramList.size();
            for (; i < length; i++) {
                T param = paramList.get(i);
                doExecute(session, param);
                boolean saveFlg = (i > 0 && i % batchSize == 0) || i == length - 1;
                if (saveFlg) {
                    session.commit();
                    session.clearCache();
                }
            }
        } catch (RuntimeException e) {
            session.rollback();
            failed(i, paramList);
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 执行sql异常时
     * @param dataIndex 发生异常时的参数记录的位置
     * @param paramList 参数List
     **/
    protected void failed(int dataIndex, List<T> paramList){

    }

    /**
     * 自定义的批量提交的执行方法
     * @param sqlSession 执行update/insert/delete方法的SqlSession对象
     * @param param 当前执行的SQL的参数对象
     * @throws RuntimeException SQL执行异常
     **/
    protected abstract void doExecute(SqlSession sqlSession, T param) throws RuntimeException;
}
