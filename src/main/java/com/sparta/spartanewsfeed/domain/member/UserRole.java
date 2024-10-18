package com.sparta.spartanewsfeed.domain.member;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }
}
