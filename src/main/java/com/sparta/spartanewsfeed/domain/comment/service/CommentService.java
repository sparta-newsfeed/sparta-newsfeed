package com.sparta.spartanewsfeed.domain.comment.service;

import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.*;

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
import com.sparta.spartanewsfeed.domain.comment.repository.CommentLikeRepository;
import com.sparta.spartanewsfeed.domain.comment.repository.CommentRepository;
import com.sparta.spartanewsfeed.domain.jwt.jwt.JwtUtil;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import com.sparta.spartanewsfeed.exception.customException.HasNotPermissionException;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;

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
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_ARTICLE));

		Member author = findByEmail(authorization);

		Comment comment = Comment.builder()
			.article(article)
			.author(author)
			.body(body)
			.build();

		return CommentResponseDto.of(commentRepository.save(comment), false);
	}

	@Transactional
	public Page<CommentResponseDto> getComments(Long articleId, int page, int size, String sortBy,
		String authorization) {
		if (!articleRepository.existsById(articleId)) {
			throw new NotFoundEntityException(NOT_FOUND_ARTICLE);
		}
		// Pageable 객체 생성 (기본값: 최신순)
		Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

		Page<Comment> comments;

		if ("popular".equals(sortBy)) {
			comments = commentLikeRepository.findCommentsByArticleOrderByLikes(articleId, pageable);
			//comments = commentRepository.findAllByArticleIdOrderByLikesCount(articleId, pageable);  // 인기순 정렬
		} else {
			comments = commentRepository.findAllByArticleId(pageable, articleId);  // 기본 최신순 정렬
		}

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
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_COMMENT));

		Member author = findByEmail(authorization);

		if (!comment.getAuthor().equals(author)) {
			throw new HasNotPermissionException(HAS_NOT_PERMISSION);
		}

		boolean isLiked = commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(), author.getId());
		comment.update(body);

		return CommentResponseDto.of(commentRepository.save(comment), isLiked);
	}

	@Transactional
	public void deleteComment(Long commentId, String authorization) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_COMMENT));

		Member author = findByEmail(authorization);

		if (!comment.getAuthor().equals(author)) {
			throw new HasNotPermissionException(HAS_NOT_PERMISSION);
		}
		commentRepository.delete(comment);
	}

	private Member findByEmail(String authorization) {
		authorization = jwtUtil.substringToken(authorization);
		Claims claims = jwtUtil.getUserInfoFromToken(authorization);

		return memberRepository.findByEmail(claims.getSubject())
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));
	}

}
