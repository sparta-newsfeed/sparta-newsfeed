package com.sparta.spartanewsfeed.exception.customException;

import com.sparta.spartanewsfeed.exception.enums.ExceptionCode;
import lombok.Getter;

@Getter
public class MakeFriendException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public MakeFriendException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
