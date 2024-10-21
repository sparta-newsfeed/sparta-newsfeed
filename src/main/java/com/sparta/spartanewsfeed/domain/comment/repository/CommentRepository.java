package com.sparta.spartanewsfeed.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findAllByArticleId(Pageable pageable, Long articleId);
}
