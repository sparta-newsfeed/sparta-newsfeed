package com.sparta.spartanewsfeed.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleRepository;
import com.sparta.spartanewsfeed.domain.comment.controller.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.domain.comment.entity.Comment;
import com.sparta.spartanewsfeed.domain.comment.repository.CommentLikeRepository;
import com.sparta.spartanewsfeed.domain.comment.repository.CommentRepository;
import com.sparta.spartanewsfeed.domain.jwt.jwt.JwtUtil;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final JwtUtil jwtUtil;

	public CommentResponseDto createComment(Long articleId, String body, String authorization) {

		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new IllegalArgumentException("Article id " + articleId + " not found"));

		Member author = findByEmail(authorization);

		Comment comment = Comment.builder()
			.article(article)
			.author(author)
			.body(body)
			.build();

		return CommentResponseDto.of(commentRepository.save(comment), false);
	}

	public Page<CommentResponseDto> getComments(Long articleId, int page, int size, String authorization) {
		if (!articleRepository.existsById(articleId)) {
			throw new IllegalArgumentException("Article id " + articleId + " not found");
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
		Page<Comment> comments = commentRepository.findAllByArticleId(pageable, articleId);

		Member author = findByEmail(authorization);

		return comments.map(comment -> {
				boolean isLiked = commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(), author.getId());
				return CommentResponseDto.of(comment, isLiked);
			}
		);
	}

	@Transactional
	public CommentResponseDto updateComment(Long commentId, String body, String authorization) {

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("Comment id " + commentId + " not found"));

		Member author = findByEmail(authorization);

		if (!comment.getAuthor().equals(author)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this comment");
		}

		boolean isLiked = commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(), author.getId());
		comment.update(body);

		return CommentResponseDto.of(commentRepository.save(comment), isLiked);
	}

	@Transactional
	public void deleteComment(Long commentId, String authorization) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("Comment id " + commentId + " not found"));

		Member author = findByEmail(authorization);

		if (!comment.getAuthor().equals(author)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this comment");
		}
		commentRepository.delete(comment);
	}

	private Member findByEmail(String authorization) {
		authorization = jwtUtil.substringToken(authorization);
		Claims claims = jwtUtil.getUserInfoFromToken(authorization);

		return memberRepository.findByEmail(claims.getSubject())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

}
