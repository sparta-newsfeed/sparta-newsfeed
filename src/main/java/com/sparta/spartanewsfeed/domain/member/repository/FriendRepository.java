package com.sparta.spartanewsfeed.domain.member.repository;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.FriendStatus;
import com.sparta.spartanewsfeed.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

	Friend findByRequestMemberAndResponseMember(Member friend, Member member);

	Page<Friend> findByStatusAndRequestMemberOrResponseMember(FriendStatus friendStatus, Member member, Member member1,
		Pageable pageable);

	@Query(value = "SELECT * FROM friend " +
			"WHERE status = 'ACCEPT' " +
			"AND ((request_id = :loginMemberId AND response_id = :targetMemberId) OR (request_id = :targetMemberId AND response_id = :loginMemberId))",
			nativeQuery = true)
	List<Friend> findAll(@Param("loginMemberId") Long loginMemberId, @Param("targetMemberId") Long targetMemberId);
}
