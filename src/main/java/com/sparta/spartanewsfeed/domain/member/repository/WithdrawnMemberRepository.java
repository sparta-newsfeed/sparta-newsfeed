package com.sparta.spartanewsfeed.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.member.WithdrawnMember;

public interface WithdrawnMemberRepository extends JpaRepository<WithdrawnMember, Long> {
	Optional<WithdrawnMember> findByEmail(String email);
}
