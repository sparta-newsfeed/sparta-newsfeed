package com.sparta.spartanewsfeed.domain.article.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
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
}
