package com.sparta.spartanewsfeed.domain.member.service;

import static com.sparta.spartanewsfeed.domain.jwt.jwt.JwtUtil.*;
import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.*;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sparta.spartanewsfeed.domain.jwt.config.PasswordEncoder;
import com.sparta.spartanewsfeed.domain.jwt.jwt.JwtUtil;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.UserRole;
import com.sparta.spartanewsfeed.domain.member.WithdrawnMember;
import com.sparta.spartanewsfeed.domain.member.dto.LoginRequestDto;
import com.sparta.spartanewsfeed.domain.member.dto.SignupRequestDto;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import com.sparta.spartanewsfeed.domain.member.repository.WithdrawnMemberRepository;
import com.sparta.spartanewsfeed.exception.customException.DuplicateEmailException;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;
import com.sparta.spartanewsfeed.exception.customException.NotMatchPasswordException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final WithdrawnMemberRepository withdrawnMemberRepository;
	private final JwtUtil jwtUtil;

	public Member signup(@Valid SignupRequestDto signupRequestDto) {
		String username = signupRequestDto.getUsername();
		String nickname = signupRequestDto.getNickname();
		UserRole role = UserRole.USER;
		String email = signupRequestDto.getEmail();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());

		// email 중복확인
		Optional<Member> checkEmail = memberRepository.findByEmail(email);
		if (checkEmail.isPresent()) {
			throw new DuplicateEmailException(DUPLICATE_EMAIL);
		}

		// 탈퇴한 이메일 여부 확인
		Optional<WithdrawnMember> checkWithdrawnEmail = withdrawnMemberRepository.findByEmail(email);
		if (checkWithdrawnEmail.isPresent()) {
			throw new DuplicateEmailException(EMAIL_HAS_HISTORY_OF_WITHDRAWAL);
		}

		if (!signupRequestDto.getPassword().equals(signupRequestDto.getCheckPassword())) {
			throw new DuplicateEmailException(NOT_MATCH_CHECK_PASSWORD);
		}

		//비밀번호 요구사항 체크는 SignRequestDto에서 설정

		// 사용자 생성 및 저장
		Member member = new Member(username, nickname, role, email, password);
		memberRepository.save(member);

		return member;
	}

	public void login(LoginRequestDto requestDto, HttpServletResponse res) {
		String email = requestDto.getEmail();
		String password = requestDto.getPassword();

		//사용자 확인
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));

		//비밀번호 확인
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new NotMatchPasswordException(NOT_MATCH_PASSWORD);
		}

		// JWT 생성 및 쿠기 저장 후 Response객체에 추가
		String token = jwtUtil.createToken(member.getEmail(), member.getRole());
		jwtUtil.addJwtToCookie(token, res);
	}

	public void logout(HttpServletResponse res) {
		Cookie cookie = new Cookie(AUTHORIZATION_HEADER, null); //쿠키 생성 후 null -> 삭제
		cookie.setHttpOnly(true); // JavaScript에 의해 접근되지 않도록
		cookie.setMaxAge(0); // 유효 시간 0
		cookie.setPath("/"); // 모든 경로에서 유효
		res.addCookie(cookie);
	}
}
