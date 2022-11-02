package com.ssibongee.daangnmarket.member.exception;

// 로그인이 안되있는 상태에서 로그인이 필요한 서비스 접근 시 발생하는 런타임 예외 정의
public class UnAuthenticatedAccessException extends RuntimeException {

    public UnAuthenticatedAccessException() {
        super();
    }

    public UnAuthenticatedAccessException(String message) {
        super(message);
    }

    public UnAuthenticatedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
