package com.sparta.spartanewsfeed.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.article.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
