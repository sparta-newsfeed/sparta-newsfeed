package com.sparta.spartanewsfeed.domain.article.controller.dto;

import java.util.List;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentResponseDto;

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
	private int commentCounts;

	public static ArticlesResponseDto from(Article article) {
		List<CommentResponseDto> commentList = article.getComments() == null ? List.of() :
			article.getComments().stream().map(CommentResponseDto::from).toList();
		return ArticlesResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.commentCounts(commentList.size())
			.body(article.getBody())
			.build();
	}
}
