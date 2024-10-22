package com.sparta.spartanewsfeed.domain.member.dto;

import com.sparta.spartanewsfeed.converter.DateTimeFormatConverter;
import com.sparta.spartanewsfeed.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseMember {
    private Long id;

    private String name;

    private String nickname;

    private String email;

    private String createdAt;

    public static ResponseMember make(Member member) {
        return ResponseMember.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .createdAt(DateTimeFormatConverter.convertDateTimeFormat(member.getCreatedAt()))
                .build();
    }
}
