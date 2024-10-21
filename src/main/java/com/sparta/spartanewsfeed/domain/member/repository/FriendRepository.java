package com.sparta.spartanewsfeed.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.FriendStatus;
import com.sparta.spartanewsfeed.domain.member.Member;

public interface FriendRepository extends JpaRepository<Friend, Long> {

	Friend findByRequestMemberAndResponseMember(Member friend, Member member);

	Page<Friend> findByStatusAndRequestMemberOrResponseMember(FriendStatus friendStatus, Member member, Member member1,
		Pageable pageable);
}
