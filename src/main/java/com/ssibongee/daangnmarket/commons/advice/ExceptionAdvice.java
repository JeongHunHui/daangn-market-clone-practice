package com.ssibongee.daangnmarket.commons.advice;

import com.ssibongee.daangnmarket.member.exception.MemberNotFoundException;
import com.ssibongee.daangnmarket.member.exception.PasswordNotMatchedException;
import com.ssibongee.daangnmarket.member.exception.UnAuthenticatedAccessException;
import com.ssibongee.daangnmarket.member.exception.UnAuthorizedAccessException;
import com.ssibongee.daangnmarket.post.exception.AreaInfoNotDefinedException;
import com.ssibongee.daangnmarket.post.exception.CategoryNotFoundException;
import com.ssibongee.daangnmarket.post.exception.PostNotFoundException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ssibongee.daangnmarket.commons.HttpStatusResponseEntity.*;


// 모든 Controller 에서 발생할 수 있는 예외를 잡아 처리해주는 어노테이션
// @ControllerAdvice의 기능을 수행하면서 @ResponseBody를 통해 객체를 리턴할 수 있음
@RestControllerAdvice
public class ExceptionAdvice {

    // @Controller, @RestController가 적용된 Bean내에서 발생하는 예외를 잡아서 하나의 메서드에서 처리
    // @ControllerAdvice안에 있는 @ExceptionHandler는 모든 컨트롤러에서 발생하는 특정 에러를 잡을 수 있다.
    // 인자로 캐치하고 싶은 에러 클래스를 넣으면 되고, 여러개를 넣을 수 있다.
    // -> 모든 컨트롤러에서 발생하는 MemberNotFoundException를 컨잡아서 NotFound를 반환한다.
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<HttpStatus> memberNotFoundException() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<HttpStatus> unAuthorizedAccessException() {
        return RESPONSE_FORBIDDEN;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> categoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AreaInfoNotDefinedException.class)
    public ResponseEntity<String> areaInfoNotDefinedException(AreaInfoNotDefinedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<HttpStatus> postNotFoundException() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(UnAuthenticatedAccessException.class)
    public ResponseEntity<HttpStatus> unAuthenticatedAccessException() {
        return RESPONSE_UNAUTHORIZED;
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<HttpStatus> passwordNotMatchedException() {
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<HttpStatus> fileSizeLimitExceededException() {
        return RESPONSE_PAYLOAD_TOO_LARGE;
    }
}
