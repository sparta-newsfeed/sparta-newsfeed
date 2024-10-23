package com.sparta.spartanewsfeed.domain.jwt.jwt;

import com.sparta.spartanewsfeed.domain.member.UserRole;
import com.sparta.spartanewsfeed.exception.customException.NotValidCookieException;
import com.sparta.spartanewsfeed.exception.customException.NotValidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.*;

@Component
public class JwtUtil {


    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";//구분하기 위해 공백

    //application.properties파일의 키 값
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    //한번만 받아와야 하는 값을 새로 호출하는 실수 방지
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //JWT 생성
    // 토큰 생성
    public String createToken(String email, UserRole role) {
        Date date = new Date();

        // 토큰 만료시간
        // 60분
        long TOKEN_TIME = 60 * 60 * 1000L;
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값(email)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    //생성된 JWT를 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    //Cookie에 들어있던 JWT 토큰을 Substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);//"Bearer " 7글자 자르고
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    //JWT 검증
    // 토큰 검증
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new NotValidTokenException(NOT_VALID_TOKEN);

        } catch (ExpiredJwtException e) {
            throw new NotValidTokenException(EXPIRED_TOKEN);

        } catch (UnsupportedJwtException e) {
            throw new NotValidTokenException(NOT_SUPPORT_TOKEN);

        } catch (IllegalArgumentException e) {
            throw new NotValidTokenException(HAS_NOT_TOKEN);
        }
    }

    //JWT에서 사용자 정보 가져오기
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) throw new NotValidCookieException(HAS_NOT_COOKIE);

        String token = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                try {
                    token = URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                } catch (UnsupportedEncodingException e) {
                    throw new NotValidTokenException(NOT_SUPPORT_TOKEN);
                }
            }
        }
        if (!StringUtils.hasText(token)) throw new NotValidTokenException(HAS_NOT_TOKEN);

        return token;
    }
}
