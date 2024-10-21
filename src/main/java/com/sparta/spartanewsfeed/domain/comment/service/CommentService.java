package com.sparta.spartanewsfeed.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

		return CommentResponseDto.from(commentRepository.save(comment));
	}

	public Page<CommentResponseDto> getComments(Long articleId, int page, int size) {
		if (!articleRepository.existsById(articleId)) {
			throw new IllegalArgumentException("Article not found");
		}
		Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

		Page<Comment> comments = commentRepository.findAllByArticleId(pageable, articleId);

		return comments.map(
			CommentResponseDto::from
		);
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
