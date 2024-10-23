package com.sparta.spartanewsfeed.domain.article.service;

import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticleResponseDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ArticlesResponseDto;
import com.sparta.spartanewsfeed.domain.article.controller.dto.ImageArticlesResponseDto;
import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.entity.ArticleImage;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleLikeRepository;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleRepository;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.service.FriendService;
import com.sparta.spartanewsfeed.exception.customException.HasNotPermissionException;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ArticleService")
@Service
@RequiredArgsConstructor
public class ArticleService {

	private final FriendService friendService;
	private final ArticleRepository articleRepository;
	private final ArticleLikeRepository articleLikeRepository;
	private final ArticleImageService articleImageService;

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

	@Transactional
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

	//----------------------------------------------------------------------------------------------------
	// 이미지 아티클 서비스

	@Transactional
	public Page<ImageArticlesResponseDto> retrieveImageArticles(Pageable pageable, Member member) {
		List<Member> friends = friendService.getRelatedFriends(member);
		// friends.add(member);
		return articleRepository.findAllByAuthorIn(friends, pageable)
			.map(ImageArticlesResponseDto::from);
	}

	@Transactional
	public ImageArticlesResponseDto retrieveImageArticle(Long id, Member member) {
		return articleRepository.findById(id)
			.map((article -> {
				boolean isLike = articleLikeRepository.findArticleLikeByArticleAndMember(article, member)
					.isPresent();
				return ImageArticlesResponseDto.of(article, isLike);
			}))
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_ARTICLE));
	}

	@Transactional
	public ImageArticlesResponseDto retrieveImageArticle(Long id) {
		return articleRepository.findById(id)
			.map((article -> {

				// 이미지 URL 리스트 생성
				List<String> imageUrls = article.getArticleImages()
					.stream()
					.map(ArticleImage::getImagePath) // ArticleImage 엔티티에서 URL 추출
					.toList();

				return ImageArticlesResponseDto.from(article);
			}))
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_ARTICLE));
	}

	@Transactional
	public ImageArticlesResponseDto createImageArticle(String title, String body, List<MultipartFile> images,
		Member member) {
		Article article = Article.builder()
			.title(title)
			.body(body)
			.author(member)
			.comments(List.of())
			.articleImages(new ArrayList<>())
			.build();

		Article savedArticle = articleRepository.save(article);

		if (images != null && !images.isEmpty()) {
			articleImageService.uploadImage(article, images);

		}
		return ImageArticlesResponseDto.from(savedArticle);
	}

	@Transactional
	public ImageArticlesResponseDto updateAImageArticle(Long id, String title, String body, List<MultipartFile> images,
		Member member) {
		Article article = articleRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_ARTICLE));
		verifyAuthorOrAdmin(article, member);
		article.update(title, body);

		// 이미지가 있을 경우 이미지 업로드 처리
		if (images != null && !images.isEmpty()) {
			// 기존 이미지를 삭제하고 새로운 이미지로 대체하는 경우라면, 기존 이미지 삭제 로직 추가 가능
			articleImageService.uploadImage(article, images);
		}

		// 업데이트된 아티클 저장
		Article savedArticle = articleRepository.save(article);

		// 업데이트된 아티클을 DTO로 변환하여 반환
		return ImageArticlesResponseDto.from(savedArticle);
	}

	// 삭제 메서드는 유지 가능
}
