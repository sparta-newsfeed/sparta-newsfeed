package com.sparta.spartanewsfeed.domain.member.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class FriendResponseDto {
	private String requestId;
	private String responseId;
	private LocalDateTime requestedAt;
	private String status;
}
