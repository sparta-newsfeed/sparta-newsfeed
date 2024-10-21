package com.sparta.spartanewsfeed.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleRepository;
import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.domain.comment.entity.Comment;
//import com.sparta.spartanewsfeed.domain.comment.repository.CommentLikeRepository;
import com.sparta.spartanewsfeed.domain.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final ArticleRepository articleRepository;
	//private final CommentLikeRepository commentLikeRepository;

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

	public void likeComment(Long commentId, String authorization) {
	}

	// 추천 유무 (아직 구현되지 않은 회원 정보를 통해 좋아요를 구현할 것으로 예상하여 작성함)
	/*
	public void likeComment(Long commentId, String authorization) {

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("Comment not found"));

		// Member member = null;
		// jwtUtil 에서 구현할 메서드 (임시)

		if (!commentLikeRepository.existsByCommentIdAndMemberId(commentId, member.getId())) {
			CommentLike commentLike = CommentLike.builder()
				.comment(comment)
				.member(member)
				.build();

			commentLikeRepository.save(commentLike);

		} else {
			commentLikeRepository.deleteByCommentIdAndMemberId(commentId, member.getId());
		}

	}
	*/

}
