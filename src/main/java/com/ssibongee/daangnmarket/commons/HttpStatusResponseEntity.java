package com.ssibongee.daangnmarket.commons;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// 중복 사용되는 HttpStatus를 반환하는 ResponseEntity를 상수로 정의
public class HttpStatusResponseEntity {

    public static final ResponseEntity<HttpStatus> RESPONSE_OK = ResponseEntity.status(HttpStatus.OK).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_CONFLICT = ResponseEntity.status(HttpStatus.CONFLICT).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_BAD_REQUEST = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_FORBIDDEN = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_PAYLOAD_TOO_LARGE = ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
}
