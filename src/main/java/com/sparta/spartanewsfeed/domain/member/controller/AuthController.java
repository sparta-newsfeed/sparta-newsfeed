package com.sparta.spartanewsfeed.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.LoginRequestDto;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;
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
	@ResponseStatus(HttpStatus.CREATED) //201
	public ResponseMember signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		Member member = authService.signup(signupRequestDto);
		System.out.println("회원가입 성공");
		return ResponseMember.make(member);
	}

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.CREATED) //201
	public ResponseEntity<Void> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
		authService.login(requestDto, res);
		return ResponseEntity
			.status(HttpStatus.OK)
			.build();
	}

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT) //204
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
