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
public class ArticleResponseDto {

	private Long id;
	private String title;
	private String body;
	private List<CommentResponseDto> comments;
	private Boolean isLiked;

	public static ArticleResponseDto from(Article article) {
		List<CommentResponseDto> commentList = article.getComments() == null ? List.of() :
			article.getComments().stream().map(CommentResponseDto::from).toList();
		return ArticleResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.comments(commentList)
			.body(article.getBody())
			.build();
	}
}
