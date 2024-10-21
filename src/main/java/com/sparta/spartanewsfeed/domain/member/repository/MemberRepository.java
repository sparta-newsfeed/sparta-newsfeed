package com.sparta.spartanewsfeed.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartanewsfeed.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}