package com.acrel.exceptions;

import com.acrel.entity.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller层通用的异常处理，每个controller只要根据业务，抛出对应的异常或返回Result对象即可，不需要再针对异常做额外处理
 * @ClassName GlobalExceptionHandler
 * @Author: ZhouChenyu
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CommonException.class)
    public Result processCommonException(CommonException e) {
        return new Result<>(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result processRuntimeException(HttpServletRequest request, RuntimeException e) {
        String url = request.getRequestURI();
        log.error("请求{}时，运行异常：{}", url,e);
        return new Result<>(new CommonException(ExceptionEnum.UNKNOW_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public Result processException(HttpServletRequest request, Exception e) {
        String url = request.getRequestURI();
        log.error("请求{}时，发生异常：{}", url, e);
        return new Result<>(ExceptionEnum.UNKNOW_ERROR);
    }

}
