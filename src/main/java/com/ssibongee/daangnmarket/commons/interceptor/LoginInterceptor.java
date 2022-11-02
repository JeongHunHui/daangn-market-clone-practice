package com.ssibongee.daangnmarket.commons.interceptor;

import com.ssibongee.daangnmarket.member.exception.UnAuthenticatedAccessException;
import com.ssibongee.daangnmarket.commons.annotation.LoginRequired;
import com.ssibongee.daangnmarket.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
// 인터셉터를 통해 로그인 처리 구현
public class LoginInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    @Override
    // preHandle: HandlerInterceptor에 정의되어있는 메소드
    // 컨트롤러가 호출되기 전에 실행됨
    // 컨트롤러가 실행 이전에 처리해야 할 작업이 있는경우 혹은 요청정보를 가공하거나 추가하는경우 사용
    // return 값이 false면 controller로 넘어가지 않화
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // HandlerMethod: @RequestMapping과 그 하위 어노테이션(@GetMapping 등)이 붙은 메소드의 정보를 추상화한 객체
        // 즉 handler가 매핑된 메소드의 정보고, 그 메소드의 어노테이션에 LoginRequired가 있으면 실행
        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class)) {
            // 세선에 저장되어 있는 로그인 정보있는지 확인
            Long memberId = loginService.getLoginMemberId();
            // 정보가 없으면 로그인이 안되어있는 것이므로 예외 발생
            if(memberId == null) {
                throw new UnAuthenticatedAccessException();
            }
        }

        return true;
    }
}
