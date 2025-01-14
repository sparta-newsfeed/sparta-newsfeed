package com.sparta.spartanewsfeed.domain.member.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.FriendStatus;
import com.sparta.spartanewsfeed.domain.member.Member;

public interface FriendRepository extends JpaRepository<Friend, Long> {

	Friend findByRequestMemberAndResponseMember(Member friend, Member member);

	Page<Friend> findByStatusAndRequestMemberOrResponseMember(FriendStatus friendStatus, Member member, Member member1,
		Pageable pageable);

	@Query("SELECT f FROM Friend f join fetch f.responseMember join fetch f.requestMember WHERE f.responseMember = :member OR f.requestMember = :member")
	List<Friend> findFriendsByMember(@Param("member") Member member);

	@Query("SELECT f FROM Friend f join fetch f.responseMember join fetch f.requestMember WHERE (f.responseMember = :member OR f.requestMember = :member) and f.status = :status")
	List<Friend> findFriendByMemberAndStatus(@Param("member") Member member, FriendStatus status);

	@Query("SELECT f FROM Friend f join fetch f.responseMember WHERE f.responseMember = :member and f.status = :status")
	List<Friend> findRequestFriendByMemberAndStatus(@Param("member") Member member, FriendStatus status);

	@Query(value = "SELECT * FROM friend " +
		"WHERE status = 'ACCEPT' " +
		"AND ((request_id = :loginMemberId AND response_id = :targetMemberId) OR (request_id = :targetMemberId AND response_id = :loginMemberId))",
		nativeQuery = true)
	List<Friend> findAll(@Param("loginMemberId") Long loginMemberId, @Param("targetMemberId") Long targetMemberId);
}
