package com.sparta.spartanewsfeed.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePassword {
    @NotBlank
    private String oldPassword;

    @NotNull
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,}$")
    private String newPassword;

    @NotBlank
    private String ConfirmNewPassword;
}
