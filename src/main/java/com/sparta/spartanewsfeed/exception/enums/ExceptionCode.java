package com.sparta.spartanewsfeed.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "Member not found"),

    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "Comment not found"),

    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "Article not found"),

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "Email address already in used"),

    NOT_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "Not match password"),

    NOT_MATCH_CHECK_PASSWORD(HttpStatus.BAD_REQUEST, "Not match origin password and checking password"),

    CURRENT_PASSWORD_AND_CHANGE_PASSWORD_IS_SAME(HttpStatus.BAD_REQUEST, "Current password and change password is same"),

    HAS_NOT_COOKIE(HttpStatus.BAD_REQUEST, "Request has not cookie"),

    NOT_SUPPORT_ENCODING_COOKIE(HttpStatus.BAD_REQUEST, "Not support encoding cookie"),

    HAS_NOT_TOKEN(HttpStatus.BAD_REQUEST, "Request has not token"),

    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "Not valid token"),

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token is expired"),

    NOT_SUPPORT_TOKEN(HttpStatus.UNAUTHORIZED, "Is not support token"),

    HAS_NOT_PERMISSION(HttpStatus.FORBIDDEN, "You do not have permission"),

    CAN_NOT_FRIEND_WITH_YOURSELF(HttpStatus.BAD_REQUEST, "Can not friend with yourself"),

    ALREADY_FRIEND(HttpStatus.BAD_REQUEST, "Already friend"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
