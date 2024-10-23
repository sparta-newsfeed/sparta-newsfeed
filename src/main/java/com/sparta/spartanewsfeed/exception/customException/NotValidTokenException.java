package com.sparta.spartanewsfeed.exception.customException;

import com.sparta.spartanewsfeed.exception.enums.ExceptionCode;
import lombok.Getter;

@Getter
public class NotValidTokenException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public NotValidTokenException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
