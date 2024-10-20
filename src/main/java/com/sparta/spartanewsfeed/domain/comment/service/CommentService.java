package com.sparta.spartanewsfeed.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartanewsfeed.domain.article.Article;
import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.domain.comment.entity.Comment;
import com.sparta.spartanewsfeed.domain.comment.repository.ArticleRepository;
import com.sparta.spartanewsfeed.domain.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final ArticleRepository articleRepository;

	public CommentResponseDto createComment(Long articleId, String body) {

		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new IllegalArgumentException("Article not found"));

		Comment comment = Comment.builder()
			.article(article)
			.author(article.getAuthor())
			.body(body)
			.build();

		// article.addComment(comment); 
		// 이와 같은 article 이 comment 등록 함수 필요, 현재는 Comment 만 Article 을 조회할 수 있는 단방향 매핑

		return CommentResponseDto.from(commentRepository.save(comment));
	}

	@Transactional
	public CommentResponseDto updateComment(Long commentId, String body) {

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("Comment not found"));

		comment.update(body);

		return CommentResponseDto.from(commentRepository.save(comment));
	}

	public void deleteComment(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("Comment not found"));
		commentRepository.delete(comment);
	}
}
