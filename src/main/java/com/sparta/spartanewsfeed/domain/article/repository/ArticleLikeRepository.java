package com.sparta.spartanewsfeed.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.entity.ArticleLike;
import com.sparta.spartanewsfeed.domain.member.Member;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

	boolean existsArticleLikeByArticleAndMember(Article article, Member member);

	void deleteArticleLikeByArticleAndMember(Article article, Member member);
}
