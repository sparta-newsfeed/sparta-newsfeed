package com.sparta.spartanewsfeed.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyIdentityResult {
    private boolean result;

    public VerifyIdentityResult(boolean result) {
        this.result = result;
    }
}
