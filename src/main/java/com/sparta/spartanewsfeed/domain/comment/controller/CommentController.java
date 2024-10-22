package com.sparta.spartanewsfeed.domain.comment.controller;

import org.springframework.data.domain.Page;
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
import com.sparta.spartanewsfeed.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/articles/{articleId}/comment")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long articleId,
		@RequestBody CommentRequestDto requestDto) {
		String body = requestDto.getBody();
		return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(articleId, body));
	}

	@GetMapping("/articles/{articleId}/comment")
	public ResponseEntity<Page<CommentResponseDto>> getComments(
		@PathVariable Long articleId,
		@RequestParam(required = false, defaultValue = "0") int page,
		@RequestParam(required = false, defaultValue = "10") int size) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(articleId, page, size));
	}

	@PutMapping("/comment/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
		@RequestBody CommentRequestDto requestDto) {
		String body = requestDto.getBody();
		return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentId, body));
	}

	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId) {
		commentService.deleteComment(commentId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/comment/{commentId}/like")
	public ResponseEntity<CommentResponseDto> likeComment(@PathVariable Long commentId,
		@CookieValue(value = "Authorization") String authorization) {
		//commentService.likeComment(commentId, authorization);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/comment/{commentId}/like")
	public ResponseEntity<CommentResponseDto> unlikeComment(@PathVariable Long commentId,
		@CookieValue(value = "Authorization") String authorization) {
		//commentService.likeComment(commentId, authorization);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
