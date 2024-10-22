package com.sparta.spartanewsfeed.domain.article.controller.dto;

import com.sparta.spartanewsfeed.domain.article.entity.Article;

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
	private boolean isLiked;

	public static ArticleResponseDto from(Article article) {
		return ArticleResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.body(article.getBody())
			.build();
	}

	public static ArticleResponseDto of(Article article, boolean isLiked) {
		return ArticleResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.isLiked(isLiked)
			.body(article.getBody())
			.build();
	}
}
