package com.sparta.spartanewsfeed.domain.article.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleCreateDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleUpdateDto;
import com.sparta.spartanewsfeed.domain.article.service.ArticleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;

	@GetMapping
	public ResponseEntity<Page<ArticleResponseDto>> retrieveArticles(Pageable pageable) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(articleService.retrieveArticles(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ArticleResponseDto> retrieveArticle(@PathVariable Long id) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(articleService.retrieveArticle(id));
	}

	@PostMapping
	public ResponseEntity<ArticleResponseDto> createArticle(@RequestBody @Valid ArticleCreateDto req) {
		String title = req.getTitle();
		String body = req.getBody();

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(articleService.createArticle(title, body));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ArticleResponseDto> updateArticle(
		@PathVariable Long id,
		@RequestBody ArticleUpdateDto req
	) {
		String title = req.getTitle();
		String body = req.getBody();

		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(articleService.updateArticle(id, title, body));
	}
}
