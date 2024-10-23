package com.sparta.spartanewsfeed.domain.article.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageArticleCreateDto {

	@NotBlank(message = "제목은 필수 입니다.")
	private String title;

	@NotBlank(message = "본문은 필수 입니다.")
	private String body;

}
