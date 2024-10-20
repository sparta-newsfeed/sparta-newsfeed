package com.sparta.spartanewsfeed.domain.comment.controller.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CommentResponseDto {

	private Long id;
	private Long articleId;
	private Long memberId;
	private String body;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
