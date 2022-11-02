package com.ssibongee.daangnmarket.commons.config;

import com.ssibongee.daangnmarket.commons.interceptor.LoginInterceptor;
import com.ssibongee.daangnmarket.commons.resolver.LoginMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
// @EnableWebMvc를 붙히면 자동적으로 필요한 설정을 세팅해줌
// WebMvcConfigurer를 사용하면 @EnableWebMvc가 자동적으로 세팅해주는 설정에
// 개발자가 원하는 설정을 추가할 수 있다.
public class WebConfig implements WebMvcConfigurer {

    // 인터셉터를 Bean에 등록
    private final LoginInterceptor loginInterceptor;
    // ArgumentResolver를 Bean에 등록
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    // 제대로 오버라이드 했는지 체크해주는 어노테이션
    @Override
    // addInterceptors를 오버라이드 하여 인터셉터 추가
    // add, excludePathPatterns() 메서드로 인터셉트 호출에서 경로를 추가, 제외할 수 있다.
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);
    }

    @Override
    // addArgumentResolvers를 오버라이드 하여 ArgumentResolver 추가
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}
