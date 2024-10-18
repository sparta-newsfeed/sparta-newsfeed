package com.sparta.spartanewsfeed.domain.member;

public enum FriendStatus {
    ACCEPT("ACCEPT"),
    PENDING("PENDING");

    private final String status;

    FriendStatus(String status) {
        this.status = status;
    }
}
