package com.sparta.spartanewsfeed.domain.article.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
import com.sparta.spartanewsfeed.domain.article.service.ArticleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;

	public ResponseEntity<Page<ArticleResponseDto>> retrieveAll(Pageable pageable) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(articleService.retrieveAll(pageable));
	}
}
