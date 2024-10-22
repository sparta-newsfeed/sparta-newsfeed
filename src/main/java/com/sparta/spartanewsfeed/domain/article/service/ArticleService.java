package com.sparta.spartanewsfeed.domain.article.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticlesResponseDto;
import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleLikeRepository;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleRepository;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.service.FriendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ArticleService")
@Service
@RequiredArgsConstructor
public class ArticleService {

	private final FriendService friendService;
	private final ArticleRepository articleRepository;
	private final ArticleLikeRepository articleLikeRepository;

	public Page<ArticlesResponseDto> retrieveArticles(Pageable pageable, Member member) {
		List<Member> friends = friendService.getRelatedFriends(member);
		return articleRepository.findAllByAuthorIn(friends, pageable)
			.map(ArticlesResponseDto::from);
	}

	public ArticleResponseDto retrieveArticle(Long id) {
		boolean isLike = articleLikeRepository.existsById(id);
		return articleRepository.findById(id)
			.map((article -> ArticleResponseDto.of(article, isLike)))
			.orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));
	}

	public ArticleResponseDto createArticle(String title, String body, Member member) {
		Article article = Article.builder()
			.title(title)
			.body(body)
			.author(member)
			.comments(List.of())
			.build();

		Article savedArticle = articleRepository.save(article);
		return ArticleResponseDto.from(savedArticle);
	}

	public ArticleResponseDto updateArticle(Long id, String title, String body, Member member) {
		Article article = articleRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));

		if (!(article.isAuthor(member.getId()) || member.isAdmin())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}

		article.update(title, body);
		Article savedArticle = articleRepository.save(article);
		return ArticleResponseDto.from(savedArticle);
	}

	public ArticleResponseDto deleteArticle(Long id) {
		// TODO: 인증/인가 개발 완료 후 삭제 권한 검증 필요
		Article article = articleRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));
		articleRepository.delete(article);
		return ArticleResponseDto.from(article);
	}
}
