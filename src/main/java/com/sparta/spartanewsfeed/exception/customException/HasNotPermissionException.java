package com.sparta.spartanewsfeed.exception.customException;

import com.sparta.spartanewsfeed.exception.enums.ExceptionCode;
import lombok.Getter;

@Getter
public class HasNotPermissionException extends RuntimeException {
    public final ExceptionCode exceptionCode;

    public HasNotPermissionException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
