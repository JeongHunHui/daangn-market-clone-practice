package com.ssibongee.daangnmarket.member.exception;

// 회원을 찾지 못했을 때 발생시킬 런타임 예외 -> RuntimeException 상속 받음
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

    // 메세지와 원인이 되는 에러를 넣어줄 cause를 받는 생성자
    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
