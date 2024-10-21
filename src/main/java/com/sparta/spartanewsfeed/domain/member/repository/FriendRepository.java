package com.sparta.spartanewsfeed.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.FriendStatus;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.FriendResponseDto;

public interface FriendRepository extends JpaRepository<Friend, Long> {

	FriendResponseDto findByRequestMember(Member member);

	FriendResponseDto findByResponseMember(Member member);

	Friend findByRequestMemberAndResponseMember(Member friend, Member member);

	FriendResponseDto findByRequestMemberAndStatus(Member member, FriendStatus friendStatus);

	FriendResponseDto findByResponseMemberAndStatus(Member member, FriendStatus friendStatus);
}
