package com.sparta.spartanewsfeed.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestVerifyIdentity {
    @NotBlank
    private String password;
}
