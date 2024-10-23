package com.sparta.spartanewsfeed.domain.article.service;

import java.io.File;
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

			// 이미지가 있는지 확인
			if (images.isEmpty()) {
				System.out.println("이미지가 없습니다.");
				return;
			}

			for (MultipartFile multipartFile : images) {
				// MultipartFile을 File로 변환
				File imageFile = convertMultipartFileToFile(multipartFile);

				if (imageFile != null) {
					String dbFilePath = saveImage(imageFile, uploadsDir);

					ArticleImage articleImage = ArticleImage.builder()
						.imagePath(dbFilePath)
						.article(article)
						.build();

					System.out.println("이미지 저장 시작: " + dbFilePath);
					article.getArticleImages().add(articleImage); // Article의 이미지 리스트에 추가
					articleImageRepository.save(articleImage);
					System.out.println("이미지 저장 완료: " + dbFilePath);

					// 임시 파일 삭제
					imageFile.delete();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// MultipartFile을 File로 변환하는 메서드
	private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString() + "_"
			+ multipartFile.getOriginalFilename());
		multipartFile.transferTo(tempFile); // 파일 전송
		return tempFile;
	}

	// 이미지 파일 저장
	private String saveImage(File image, String uploadsDir) throws IOException {
		// 파일이름 생성
		String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getName();

		// 파일 저장 경로
		String filePath = uploadsDir + fileName;

		// db 에 저장할 경로 문자
		String dbFilePath = "/uploads/images/" + fileName;

		// 디렉토리 생성과정
		Path path = Paths.get(filePath);
		System.out.println(path);
		Files.createDirectories(path.getParent());
		Files.write(path, Files.readAllBytes(image.toPath())); // 파일 저장

		return dbFilePath;
	}
}
