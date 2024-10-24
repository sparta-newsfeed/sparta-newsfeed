package com.sparta.spartanewsfeed;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentRequestDto;
import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.domain.comment.service.CommentService;
import com.sparta.spartanewsfeed.domain.member.Member;

@SpringBootTest
public class Test1 {

	@MockBean
	private CommentService commentService;

	@MockBean
	private CommentRequestDto commentRequestDto;

	@MockBean
	private CommentResponseDto commentResponseDto;

	@MockBean
	private Member member;

	String authorization = "Bearer token";

	@BeforeEach
	void beforeEach() {
		System.out.println("테스트 시작");

		// commentRequestDto의 body 값 설정
		Mockito.when(commentRequestDto.getBody()).thenReturn("Test comment");

		// commentService.createComment에 대한 Mock 행동 정의
		Mockito.when(commentService.createComment(anyLong(), anyString(), anyString()))
			.thenReturn(commentResponseDto);

	}

	@Test
	void addTest() {
		// 주어진 데이터를 바탕으로 서비스 호출
		CommentResponseDto result = commentService.createComment(1L, commentRequestDto.getBody(), authorization);

		// 결과에 대한 검증
		assertNotNull(result);
		assertEquals(commentResponseDto, result);

		// createComment 가 호출되었는지 검증
		Mockito.verify(commentService).createComment(1L, "Test comment", authorization);

	}

}
