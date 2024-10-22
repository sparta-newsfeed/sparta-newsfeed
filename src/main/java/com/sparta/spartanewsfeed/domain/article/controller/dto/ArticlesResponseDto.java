package com.sparta.spartanewsfeed.domain.article.controller.dto;

import java.time.LocalDateTime;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ArticlesResponseDto {

	private Long id;
	private String title;
	private String body;
	private ResponseMember author;
	private LocalDateTime updatedAt;
	private int commentCounts;

	public static ArticlesResponseDto from(Article article) {
		Member author = article.getAuthor();
		ResponseMember responseAuthor = ResponseMember.make(author);
		return ArticlesResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.commentCounts(article.getComments().size())
			.author(responseAuthor)
			.body(article.getBody())
			.updatedAt(article.getUpdatedAt())
			.build();
	}
}
