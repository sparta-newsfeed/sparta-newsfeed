package com.sparta.spartanewsfeed.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateInfo {
    @NotBlank
    private String name;

    @NotBlank
    private String nickname;
}
