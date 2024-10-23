package com.sparta.spartanewsfeed.exception;

import com.sparta.spartanewsfeed.exception.customException.*;
import com.sparta.spartanewsfeed.exception.dto.NotValidRequestParameter;
import com.sparta.spartanewsfeed.exception.dto.ResponseExceptionCode;
import com.sparta.spartanewsfeed.exception.enums.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.sparta.spartanewsfeed.exception.dto.NotValidRequestParameter.NotValidParameter;
import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.INVALID_REQUEST_PARAMETER;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotValidCookieException.class)
    public ResponseEntity<Object> handleNotValidCookieException(NotValidCookieException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode, e.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(NotValidTokenException.class)
    public ResponseEntity<Object> handleNotValidTokenException(NotValidTokenException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode.name(), e.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity<Object> handleNotMatchPasswordException(NotMatchPasswordException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode.name(), e.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode.name(), e.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(HasNotPermissionException.class)
    public ResponseEntity<Object> handleHasNotPermissionException(HasNotPermissionException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode.name(), e.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<Object> handleNotFoundEntityException(NotFoundEntityException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode.name(), e.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    private ResponseExceptionCode makeResponseExceptionCode(ExceptionCode exceptionCode) {
        return ResponseExceptionCode.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ExceptionCode exceptionCode = INVALID_REQUEST_PARAMETER;
        log.error("{}: {}", exceptionCode.name(), e.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeNotValidRequestParameter(e, exceptionCode));
    }

    private NotValidRequestParameter makeNotValidRequestParameter(BindException e,
                                                                  ExceptionCode exceptionCode) {
        List<NotValidParameter> notValidParameters = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(NotValidParameter::of)
                .toList();

        return NotValidRequestParameter.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .notValidParameters(notValidParameters)
                .build();
    }
}
