package com.sparta.spartanewsfeed.domain.member.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseMember {
    private String name;

    private String nickname;

    private String email;

    private String createdAt;
}
