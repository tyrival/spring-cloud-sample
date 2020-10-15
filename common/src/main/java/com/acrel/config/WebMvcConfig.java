package com.acrel.config;

import com.acrel.annotation.mvc.CurrentUser;
import com.acrel.entity.base.Token;
import com.acrel.entity.auth.User;
import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 添加自定义的MVC配置
 * @Author ZhouChenyu
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserMethodArgumentResolver());
    }

    /**
     * 用于在给定请求的上下文中，获取设置在当前请求中的用户信息，并将方法参数解析为User对象
     * 用户信息由前端token解析生成
     */
    class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(CurrentUser.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            // 当前用户信息以字符串的格式存放在Request的头信息中（详见Zuul模块的TokenFilter.java）
            String userString = webRequest.getHeader(Token.REQUEST_ATTRIBUTE_USER);
            if (userString == null || "".equals(userString)) {
                return null;
            }
            userString = URLDecoder.decode(userString, StandardCharsets.UTF_8.name());
            Gson objectMapper = new Gson();
            return objectMapper.fromJson(userString, User.class);
        }
    }
}
