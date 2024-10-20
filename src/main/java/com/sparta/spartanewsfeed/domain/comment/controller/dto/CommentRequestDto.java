package com.sparta.spartanewsfeed.domain.comment.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {

	@NotNull(message = "댓글 내용은 필수입니다.")
	private String body;
}
