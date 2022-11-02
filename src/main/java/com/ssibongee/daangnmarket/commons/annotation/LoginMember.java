package com.ssibongee.daangnmarket.commons.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 로그인 된 사용자 정보임을 나타내는 어노테이션
public @interface LoginMember {
}
