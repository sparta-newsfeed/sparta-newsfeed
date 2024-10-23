package com.sparta.spartanewsfeed.domain.article.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleCreateDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleUpdateDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticlesResponseDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ImageArticleCreateDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ImageArticlesResponseDto;
import com.sparta.spartanewsfeed.domain.article.service.ArticleLikeService;
import com.sparta.spartanewsfeed.domain.article.service.ArticleService;
import com.sparta.spartanewsfeed.domain.member.Member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;
	private final ArticleLikeService articleLikeService;

	@GetMapping
	public ResponseEntity<PagedModel<ArticlesResponseDto>> retrieveArticles(
		@RequestAttribute("member") Member member,
		@PageableDefault Pageable pageable
	) {
		Page<ArticlesResponseDto> articles = articleService.retrieveArticles(pageable, member);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new PagedModel<>(articles));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ArticleResponseDto> retrieveArticle(
		@PathVariable Long id,
		@RequestAttribute("member") Member member
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(articleService.retrieveArticle(id, member));
	}

	@PostMapping
	public ResponseEntity<ArticleResponseDto> createArticle(
		@RequestAttribute("member") Member member,
		@RequestBody @Valid ArticleCreateDto req
	) {
		String title = req.getTitle();
		String body = req.getBody();

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(articleService.createArticle(title, body, member));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ArticleResponseDto> updateArticle(
		@RequestAttribute("member") Member member,
		@PathVariable Long id,
		@RequestBody ArticleUpdateDto req
	) {
		String title = req.getTitle();
		String body = req.getBody();

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(articleService.updateArticle(id, title, body, member));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ArticleResponseDto> deleteArticle(
		@RequestAttribute("member") Member member,
		@PathVariable Long id
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(articleService.deleteArticle(id, member));
	}

	@PostMapping("/{articleId}/like")
	public ResponseEntity<Void> toggleLike(
		@RequestAttribute("member") Member member,
		@PathVariable Long articleId
	) {
		articleLikeService.toggleLike(articleId, member);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	//---------------------------------------------------------------------------------------------
	// 추가 이미지 컨트롤러

	@GetMapping("/image")
	public ResponseEntity<PagedModel<ImageArticlesResponseDto>> retrieveImageArticles(
		@RequestAttribute("member") Member member,
		@PageableDefault Pageable pageable
	) {
		Page<ImageArticlesResponseDto> articles = articleService.retrieveImageArticles(pageable, member);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new PagedModel<>(articles));
	}

	@GetMapping("/image/{id}")
	public ResponseEntity<ImageArticlesResponseDto> retrieveImageArticle(
		@PathVariable Long id,
		@RequestAttribute("member") Member member
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(articleService.retrieveImageArticle(id, member));
	}

	@PostMapping("/image")
	public ResponseEntity<ImageArticlesResponseDto> createImageArticle(
		@RequestAttribute("member") Member member,
		@RequestParam(value = "images", required = false) List<MultipartFile> images,
		@ModelAttribute ImageArticleCreateDto req
	) {
		String title = req.getTitle();
		String body = req.getBody();

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(articleService.createImageArticle(title, body, images, member));
	}

	@PatchMapping("/image/{id}")
	public ResponseEntity<ImageArticlesResponseDto> updateImageArticle(
		@PathVariable Long id,
		@RequestAttribute("member") Member member,
		@RequestParam(value = "images", required = false) List<MultipartFile> images,
		@ModelAttribute ImageArticleCreateDto req // 업데이트할 필드를 포함하는 DTO
	) {
		// Service를 호출하여 이미지를 포함한 아티클을 업데이트
		ImageArticlesResponseDto updatedArticle = articleService.updateAImageArticle(id, req.getTitle(), req.getBody(),
			images, member);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(updatedArticle);
	}

}
