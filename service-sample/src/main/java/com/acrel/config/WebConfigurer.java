package com.acrel.config;

import com.acrel.interceptor.TokenInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 拦截器注册
 *
 * @Author ZhouChenyu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "interceptor")
public class WebConfigurer implements WebMvcConfigurer {

    private List<String> tokenExclude;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(tokenExclude);
    }
}