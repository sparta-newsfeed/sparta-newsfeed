package com.sparta.spartanewsfeed.domain.article.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.entity.ArticleImage;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageArticlesResponseDto {
	private Long id;
	private String title;
	private String body;
	private ResponseMember author;
	private boolean isLiked;
	private List<String> articleImageUrls;
	private LocalDateTime updatedAt;

	public static ImageArticlesResponseDto from(Article article) {
		ResponseMember responseAuthor = ResponseMember.make(article.getAuthor());

		List<String> imageUrls = Optional.ofNullable(article.getArticleImages())
			.orElse(List.of()) // null일 경우 빈 리스트 반환
			.stream()
			.map(ArticleImage::getImagePath)
			.toList();

		return ImageArticlesResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.author(responseAuthor)
			.updatedAt(article.getUpdatedAt())
			.body(article.getBody())
			.articleImageUrls(imageUrls)
			.build();
	}

	public static ImageArticlesResponseDto of(Article article, boolean isLiked) {
		ResponseMember responseAuthor = ResponseMember.make(article.getAuthor());

		List<String> imageUrls = Optional.ofNullable(article.getArticleImages())
			.orElse(List.of()) // null일 경우 빈 리스트 반환
			.stream()
			.map(ArticleImage::getImagePath)
			.toList();

		return ImageArticlesResponseDto.builder()
			.id(article.getId())
			.title(article.getTitle())
			.author(responseAuthor)
			.isLiked(isLiked)
			.body(article.getBody())
			.articleImageUrls(imageUrls)
			.updatedAt(article.getUpdatedAt())
			.build();
	}

}
