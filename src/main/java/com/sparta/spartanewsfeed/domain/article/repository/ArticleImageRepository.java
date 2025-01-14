package com.sparta.spartanewsfeed.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.article.entity.ArticleImage;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
}
