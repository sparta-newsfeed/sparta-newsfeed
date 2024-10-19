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

	public Page<ArticleResponseDto> retrieveAll(Pageable pageable) {
		return articleRepository.findAll(pageable)
			.map(ArticleResponseDto::from);
	}
}
