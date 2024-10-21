package com.sparta.spartanewsfeed.domain.article.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;

	public Page<ArticleResponseDto> retrieveArticles(Pageable pageable) {
		return articleRepository.findAll(pageable)
			.map(ArticleResponseDto::from);
	}

	public ArticleResponseDto retrieveArticle(Long id) {
		return articleRepository.findById(id)
			.map(ArticleResponseDto::from)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));
	}

	public ArticleResponseDto createArticle(String title, String body) {
		// TODO: 멤버 개발 완료 되면 인증된 멤버 가져와서 article객체에 설정 필요
		Article article = Article.builder()
			.title(title)
			.body(body)
			.build();

		Article savedArticle = articleRepository.save(article);
		return ArticleResponseDto.from(savedArticle);
	}

	public ArticleResponseDto updateArticle(Long id, String title, String body) {
		// TODO: 인증/인가 개발 완료 후 수정 권한 검증 필요
		Article article = articleRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));
		article.update(title, body);
		Article savedArticle = articleRepository.save(article);
		return ArticleResponseDto.from(savedArticle);
	}

	public ArticleResponseDto deleteArticle(Long id) {
		// TODO: 인증/인가 개발 완료 후 삭제 권한 검증 필요
		Article article = articleRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));
		articleRepository.delete(article);
		return ArticleResponseDto.from(article);
	}
}
