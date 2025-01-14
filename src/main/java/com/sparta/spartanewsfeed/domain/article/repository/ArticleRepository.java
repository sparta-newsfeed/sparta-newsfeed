package com.sparta.spartanewsfeed.domain.article.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.member.Member;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	@Query("SELECT distinct a FROM Article a WHERE a.author IN :authors")
	Page<Article> findAllByAuthorIn(@Param("authors") List<Member> authors, Pageable pageable);
}
