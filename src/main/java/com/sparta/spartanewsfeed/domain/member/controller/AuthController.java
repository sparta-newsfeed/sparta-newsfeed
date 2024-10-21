package com.sparta.spartanewsfeed.domain.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.member.dto.LoginRequestDto;
import com.sparta.spartanewsfeed.domain.member.dto.SignupRequestDto;
import com.sparta.spartanewsfeed.domain.member.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(final AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/signup")
	public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		if (!signupRequestDto.getPassword().equals(signupRequestDto.getCheckPassword())) {
			throw new IllegalArgumentException("비밀번호를 확인하세요");
		}

		authService.signup(signupRequestDto);
		System.out.println("회원가입 성공");
		return "회원가입 성공";
	}

	@PostMapping("/login")
	public String login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
		authService.login(requestDto, res);
		System.out.println("로그인");
		return "로그인";
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse res) {
		Cookie[] cookies = request.getCookies();
		// //버튼 구현하면 필요 없을...?
		// if (cookies == null) {
		// 	throw new IllegalArgumentException("로그아웃 상태입니다");
		// }
		authService.logout(res);

		System.out.println("로그아웃");
		return "로그아웃";
	}
}
