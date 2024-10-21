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

	public static ArticleResponseDto from(Article article) {
		return ArticleResponseDto.builder()
			.title(article.getTitle())
			.body(article.getBody())
			.build();
	}
}
