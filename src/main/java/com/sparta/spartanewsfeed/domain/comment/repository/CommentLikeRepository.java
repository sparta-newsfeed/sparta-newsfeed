package com.sparta.spartanewsfeed.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.comment.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

	void deleteByCommentIdAndMemberId(Long commentId, Long memberId);
}
