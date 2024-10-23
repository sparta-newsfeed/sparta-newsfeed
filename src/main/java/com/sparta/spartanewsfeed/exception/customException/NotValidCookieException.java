package com.sparta.spartanewsfeed.exception.customException;

import com.sparta.spartanewsfeed.exception.enums.ExceptionCode;
import lombok.Getter;

@Getter
public class NotValidCookieException extends RuntimeException {
    public final ExceptionCode exceptionCode;

    public NotValidCookieException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
