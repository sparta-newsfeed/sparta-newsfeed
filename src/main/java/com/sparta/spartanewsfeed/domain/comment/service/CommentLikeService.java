package com.sparta.spartanewsfeed.domain.comment.service;

import com.sparta.spartanewsfeed.domain.comment.entity.Comment;
import com.sparta.spartanewsfeed.domain.comment.entity.CommentLike;
import com.sparta.spartanewsfeed.domain.comment.repository.CommentLikeRepository;
import com.sparta.spartanewsfeed.domain.comment.repository.CommentRepository;
import com.sparta.spartanewsfeed.domain.jwt.jwt.JwtUtil;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.NOT_FOUND_COMMENT;
import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public void likeComment(Long commentId, String authorization) {

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_COMMENT));
//			.orElseThrow(() -> new IllegalArgumentException("Comment id " + commentId + " not found"));

		Member member = findByEmail(authorization);

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

	private Member findByEmail(String authorization) {
		authorization = jwtUtil.substringToken(authorization);
		Claims claims = jwtUtil.getUserInfoFromToken(authorization);

		return memberRepository.findByEmail(claims.getSubject())
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));
//			.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}
