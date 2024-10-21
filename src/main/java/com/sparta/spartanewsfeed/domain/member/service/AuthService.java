package com.sparta.spartanewsfeed.domain.member.service;

import static com.sparta.spartanewsfeed.domain.jwt.jwt.JwtUtil.*;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.jwt.config.PasswordEncoder;
import com.sparta.spartanewsfeed.domain.member.dto.LoginRequestDto;
import com.sparta.spartanewsfeed.domain.member.dto.SignupRequestDto;
import com.sparta.spartanewsfeed.domain.jwt.jwt.JwtUtil;
import com.sparta.spartanewsfeed.domain.member.repository.AuthRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthRepository authRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public void signup(@Valid SignupRequestDto signupRequestDto) {
		String username = signupRequestDto.getUsername();
		String nickname = signupRequestDto.getNickname();
		String email = signupRequestDto.getEmail();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());

		// email 중복확인
		Optional<Member> checkEmail = authRepository.findByEmail(email);
		if (checkEmail.isPresent()) {
			throw new IllegalArgumentException("중복된 Email 입니다.");
		}
		//비밀번호 체크는 SignRequestDto에서 설정

		// 사용자 생성 및 저장
		Member member = new Member(username, nickname, email, password);
		authRepository.save(member);
	}

	public void login(LoginRequestDto requestDto, HttpServletResponse res) {
		String email = requestDto.getEmail();
		String password = requestDto.getPassword();

		//사용자 확인
		Member member = authRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다"));

		//비밀번호 확인
		if(!passwordEncoder.matches(password, member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
		}

		// JWT 생성 및 쿠기 저장 후 Response객체에 추가
		String token = jwtUtil.createToken(member.getEmail());
		jwtUtil.addJwtToCookie(token ,res);
	}

	public void logout(HttpServletResponse res) {
		Cookie cookie = new Cookie(AUTHORIZATION_HEADER, null); //쿠키 생성 후 null -> 삭제
		cookie.setHttpOnly(true); // JavaScript에 의해 접근되지 않도록
		cookie.setMaxAge(0); // 유효 시간 0
		cookie.setPath("/"); // 모든 경로에서 유효
		res.addCookie(cookie);
	}
}
