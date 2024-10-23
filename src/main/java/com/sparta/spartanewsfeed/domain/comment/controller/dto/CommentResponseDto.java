package com.sparta.spartanewsfeed.domain.comment.controller.dto;

import java.time.LocalDateTime;

import com.sparta.spartanewsfeed.domain.comment.entity.Comment;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {

	private Long id;
	private Long articleId;
	private ResponseMember commenter;
	private String body;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isLiked; // 해당 유저가 해당 댓글에 좋아요 유무

	public static CommentResponseDto of(Comment comment, boolean isLiked) {
		ResponseMember responseAuthor = ResponseMember.make(comment.getAuthor());
		return CommentResponseDto.builder()
			.id(comment.getId())
			.articleId(comment.getArticle().getId())
			.commenter(responseAuthor)
			.body(comment.getBody())
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt())
			.isLiked(isLiked)
			.build();
	}

}
