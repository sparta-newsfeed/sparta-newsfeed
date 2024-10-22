package com.sparta.spartanewsfeed.domain.member.dto;

import java.time.LocalDateTime;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.FriendStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FriendResponseDto {

	private Long requestId;
	private Long responseId;
	private LocalDateTime requestedAt;
	private FriendStatus friendStatus;

	public FriendResponseDto(Friend friend) {
		this.requestId = friend.getRequestMember().getId();
		this.responseId = friend.getResponseMember().getId();
		this.requestedAt = friend.getRequestedAt();
		this.friendStatus = friend.getStatus();
	}
}
