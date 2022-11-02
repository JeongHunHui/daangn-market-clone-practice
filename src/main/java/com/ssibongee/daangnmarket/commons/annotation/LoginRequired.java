package com.ssibongee.daangnmarket.commons.annotation;

import java.lang.annotation.*;

// 어노테이션의 적용 대상(METHOD: 메소드)
@Target({ElementType.METHOD})
// 어노테이션의 유지 기간 지정(RUNTIME: 클래스 파일에 존재, 실행시에 사용가능)
@Retention(RetentionPolicy.RUNTIME)
// javadoc으로 작성한 문서에 포함
@Documented
// 로그인이 필요한 메소드에 붙혀서 로그인상태인지 체크하도록 하는 어노테이션
public @interface LoginRequired {
}
