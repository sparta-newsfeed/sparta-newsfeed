package com.sparta.spartanewsfeed.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.article.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
