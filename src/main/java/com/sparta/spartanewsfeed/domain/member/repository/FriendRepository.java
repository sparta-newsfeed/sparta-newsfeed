package com.sparta.spartanewsfeed.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.FriendReponseDto;

public interface FriendRepository extends JpaRepository<Friend, Long> {

	FriendReponseDto findByRequestMember(Member member);

	FriendReponseDto findByResponseMember(Member member);

	Friend findByRequestMemberAndResponseMember(Member friend, Member member);
}
