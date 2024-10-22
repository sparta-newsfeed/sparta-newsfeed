package com.sparta.spartanewsfeed.domain.member.repository;

import com.sparta.spartanewsfeed.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	@Query(value = "SELECT m FROM Member AS m " +
			"WHERE (m.name LIKE CONCAT('%', :nameOrNickname, '%') OR m.nickname LIKE CONCAT('%', :nameOrNickname, '%')) " +
			"AND m.id > :lastMemberId ORDER BY m.id LIMIT 5")
	List<Member> findAll(@Param("nameOrNickname") String nameOrNickname, @Param("lastMemberId") Long lastMemberId);

	Optional<Member> findByEmail(String email);
}
