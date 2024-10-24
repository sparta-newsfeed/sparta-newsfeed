package com.sparta.spartanewsfeed.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.spartanewsfeed.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.article.id = :articleId")
	Page<Comment> findAllByArticleId(Pageable pageable, @Param("articleId") Long articleId);
}
