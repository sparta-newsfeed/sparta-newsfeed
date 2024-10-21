package com.sparta.spartanewsfeed.domain.member.dto;

import com.sparta.spartanewsfeed.domain.member.UserRole;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class SignupRequestDto {
	private String username;

	private String nickname;

	private UserRole role;

	@NotBlank(message = "사용자 아이디는 필수입니다.")
	@Email(message = "이메일 형식이어야 합니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,}$", message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하세요")
	private String password;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,}$", message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하세요")
	private String checkPassword;
}
