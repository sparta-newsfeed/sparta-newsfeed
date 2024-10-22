package com.sparta.spartanewsfeed.domain.comment.controller.dto;

import java.time.LocalDateTime;

import com.sparta.spartanewsfeed.domain.comment.entity.Comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {

	private Long id;
	private Long articleId;
	private Long memberId;
	private String body;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isLiked; // 해당 유저가 해당 댓글에 좋아요 유무

	public static CommentResponseDto from(Comment comment) {
		return CommentResponseDto.builder()
			.id(comment.getId())
			.articleId(comment.getArticle().getId())
			.memberId(comment.getAuthor().getId())
			.body(comment.getBody())
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt())
			.build();
	}

	public static CommentResponseDto of(Comment comment, boolean isLiked) {
		return CommentResponseDto.builder()
			.id(comment.getId())
			.articleId(comment.getArticle().getId())
			.memberId(comment.getAuthor().getId())
			.body(comment.getBody())
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt())
			.isLiked(isLiked)
			.build();
	}

}
