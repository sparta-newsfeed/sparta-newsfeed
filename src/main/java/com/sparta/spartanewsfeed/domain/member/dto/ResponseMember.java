package com.sparta.spartanewsfeed.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseMember {
    private String name;

    private String nickname;

    private String email;

    private String role;
}
