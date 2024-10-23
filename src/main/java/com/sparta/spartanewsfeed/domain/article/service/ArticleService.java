package com.sparta.spartanewsfeed.domain.article.service;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticlesResponseDto;
import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleLikeRepository;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleRepository;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.service.FriendService;
import com.sparta.spartanewsfeed.exception.customException.HasNotPermissionException;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.HAS_NOT_PERMISSION;
import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.NOT_FOUND_ARTICLE;

@Slf4j(topic = "ArticleService")
@Service
@RequiredArgsConstructor
public class ArticleService {

	private final FriendService friendService;
	private final ArticleRepository articleRepository;
	private final ArticleLikeRepository articleLikeRepository;

	public Page<ArticlesResponseDto> retrieveArticles(Pageable pageable, Member member) {
		List<Member> friends = friendService.getRelatedFriends(member);
		// friends.add(member);
		return articleRepository.findAllByAuthorIn(friends, pageable)
			.map(ArticlesResponseDto::from);
	}

	public ArticleResponseDto retrieveArticle(Long id, Member member) {
		return articleRepository.findById(id)
			.map((article -> {
				boolean isLike = articleLikeRepository.findArticleLikeByArticleAndMember(article, member)
					.isPresent();
				return ArticleResponseDto.of(article, isLike);
			}))
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_ARTICLE));
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
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_ARTICLE));
		verifyAuthorOrAdmin(article, member);
		article.update(title, body);
		Article savedArticle = articleRepository.save(article);
		return ArticleResponseDto.from(savedArticle);
	}

	public ArticleResponseDto deleteArticle(Long id, Member member) {
		Article article = articleRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_ARTICLE));
		verifyAuthorOrAdmin(article, member);
		articleRepository.delete(article);
		return ArticleResponseDto.from(article);
	}

	private void verifyAuthorOrAdmin(Article article, Member member) {
		if (!(article.isAuthor(member.getId()) || member.isAdmin())) {
			throw new HasNotPermissionException(HAS_NOT_PERMISSION);
		}
	}
}
