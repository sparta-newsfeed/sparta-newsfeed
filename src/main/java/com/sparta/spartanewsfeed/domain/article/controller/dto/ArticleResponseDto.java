package com.sparta.spartanewsfeed.domain.article.controller.dto;

import java.time.LocalDateTime;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ArticleResponseDto {

	private Long id;
	private String title;
	private String body;
	private ResponseMember author;
	private boolean isLiked;
	private LocalDateTime updatedAt;

	public static ArticleResponseDto from(Article article) {
		ResponseMember responseAuthor = ResponseMember.make(article.getAuthor());
		return ArticleResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.author(responseAuthor)
			.updatedAt(article.getUpdatedAt())
			.body(article.getBody())
			.build();
	}

	public static ArticleResponseDto of(Article article, boolean isLiked) {
		ResponseMember responseAuthor = ResponseMember.make(article.getAuthor());
		return ArticleResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.author(responseAuthor)
			.isLiked(isLiked)
			.body(article.getBody())
			.updatedAt(article.getUpdatedAt())
			.build();
	}
}
