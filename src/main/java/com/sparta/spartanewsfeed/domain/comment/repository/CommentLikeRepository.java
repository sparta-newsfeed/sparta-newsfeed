package com.sparta.spartanewsfeed.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.spartanewsfeed.domain.comment.entity.Comment;
import com.sparta.spartanewsfeed.domain.comment.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

	void deleteByCommentIdAndMemberId(Long commentId, Long memberId);

	@Query("SELECT c FROM Comment c " +
		"LEFT JOIN CommentLike cl ON cl.comment.id = c.id " +
		"WHERE c.article.id = :articleId " +
		"GROUP BY c.id " +
		"ORDER BY COUNT(cl.id) DESC")
	Page<Comment> findCommentsByArticleOrderByLikes(@Param("articleId") Long articleId, Pageable pageable);

}

