package com.sparta.spartanewsfeed.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.article.entity.ArticleLike;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
}
