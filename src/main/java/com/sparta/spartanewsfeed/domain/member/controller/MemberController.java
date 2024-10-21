package com.sparta.spartanewsfeed.domain.member.controller;

import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.LoginRequestDto;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;
import com.sparta.spartanewsfeed.domain.member.dto.SignupRequestDto;
import com.sparta.spartanewsfeed.domain.member.dto.UpdateInfo;
import com.sparta.spartanewsfeed.domain.member.dto.UpdatePassword;
import com.sparta.spartanewsfeed.domain.member.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService memberService;

	public MemberController(final MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/{memberId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseMember findById(@PathVariable(name = "memberId") Long memberId) {
		return memberService.findById(memberId);
	}

	@PutMapping("/info")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateInfo(@RequestAttribute(name = "member") Member member,
		@RequestBody @Valid UpdateInfo request) {
		memberService.updateInfo(member, request);
	}

	@PutMapping("/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updatePassword(@RequestAttribute(name = "member") Member member,
		@RequestBody @Valid UpdatePassword request) {
		memberService.updatePassword(member, request);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@RequestAttribute(name = "member") Member member) {
		memberService.delete(member);
	}

	@PostMapping("/signup")
	public String  signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		if (!signupRequestDto.getPassword().equals(signupRequestDto.getCheckPassword())) {
			throw new IllegalArgumentException("비밀번호를 확인하세요");
		}

		memberService.signup(signupRequestDto);
		System.out.println("회원가입 성공");
		return "회원가입 성공";
	}

	@PostMapping("/login")
	public String login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
		memberService.login(requestDto, res);
		System.out.println("로그인");
		return "로그인";
	}

	@PostMapping("/logout")
	public String  logout(HttpServletRequest request, HttpServletResponse res) {
		Cookie[] cookies = request.getCookies();
		// //버튼 구현하면 필요 없을...?
		// if (cookies == null) {
		// 	throw new IllegalArgumentException("로그아웃 상태입니다");
		// }
		// memberService.logout(res);

		System.out.println("로그아웃");
		return "로그아웃";
	}
}
