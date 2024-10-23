package com.sparta.spartanewsfeed.domain.article.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.spartanewsfeed.domain.article.entity.Article;
import com.sparta.spartanewsfeed.domain.article.entity.ArticleImage;
import com.sparta.spartanewsfeed.domain.article.repository.ArticleImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleImageService {

	private final ArticleImageRepository articleImageRepository;

	// 이미지 등록
	@Transactional(rollbackFor = Exception.class)
	public void uploadImage(Article article, List<MultipartFile> images) {
		try {
			// 저장 경로 설정
			String uploadsDir = "src/main/resources/static/uploads/images/";

			for (MultipartFile image : images) {
				String dbFilePath = saveImage(image, uploadsDir);

				ArticleImage articleImage = ArticleImage.builder()
					.imagePath(dbFilePath)
					.article(article)
					.build();
				
				article.getArticleImages().add(articleImage); // Article의 이미지 리스트에 추가
				articleImageRepository.save(articleImage);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 이미지 파일 저장
	private String saveImage(MultipartFile image, String uploadsDir) throws IOException {
		// 파일이름 생성
		String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();

		// 파일 저장 경로
		String filePath = uploadsDir + fileName;

		// db 에 저장할 경로 문자
		String dbFilePath = "/uploads/images/" + fileName;

		// 디렉토리 생성과정
		Path path = Paths.get(filePath);
		Files.createDirectories(path.getParent());
		Files.write(path, image.getBytes()); // 파일 저장

		return dbFilePath;
	}
}
