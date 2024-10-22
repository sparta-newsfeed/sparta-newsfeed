package com.sparta.spartanewsfeed.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OtherMemberProfile {
    private ResponseMember responseMember;

    private boolean isFriend;
}
