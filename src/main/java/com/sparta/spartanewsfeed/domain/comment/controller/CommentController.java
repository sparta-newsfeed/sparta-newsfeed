package com.sparta.spartanewsfeed.domain.comment.controller;

import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentRequestDto;
import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.domain.comment.service.CommentLikeService;
import com.sparta.spartanewsfeed.domain.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

	private final CommentService commentService;
	private final CommentLikeService commentLikeService;

	@PostMapping("/articles/{articleId}/comment")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long articleId,
		@Valid @RequestBody CommentRequestDto requestDto,
		@CookieValue(value = "Authorization", required = false) String authorization) {
		//@RequestHeader(value = "Authorization") String authorization) { // 쿠키가 아닌 헤더에 담는 경우 (모바일에서는 이걸 더 선호함)
		String body = requestDto.getBody();
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(commentService.createComment(articleId, body, authorization));
	}

	@GetMapping("/articles/{articleId}/comment")
	public ResponseEntity<PagedModel<CommentResponseDto>> getComments(
		@PathVariable Long articleId,
		@RequestParam(required = false, defaultValue = "0") int page,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(required = false, defaultValue = "latest") String sortBy,
		@CookieValue(value = "Authorization", required = false) String authorization) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(new PagedModel<>(commentService.getComments(articleId, page, size, sortBy, authorization)));
	}

	@PutMapping("/comment/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
		@Valid @RequestBody CommentRequestDto requestDto,
		@CookieValue(value = "Authorization") String authorization) {
		String body = requestDto.getBody();
		return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentId, body, authorization));
	}

	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId,
		@CookieValue(value = "Authorization") String authorization) {
		commentService.deleteComment(commentId, authorization);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/comment/{commentId}/like")
	public ResponseEntity<CommentResponseDto> likeComment(@PathVariable Long commentId,
		@CookieValue(value = "Authorization") String authorization) {
		commentLikeService.likeComment(commentId, authorization);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
