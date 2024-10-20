package com.sparta.spartanewsfeed.domain.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentRequestDto;
import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{articleId}/comment")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long articleId,
		@RequestBody CommentRequestDto requestDto) {
		String body = requestDto.getBody();
		return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(articleId, body));
	}

}
