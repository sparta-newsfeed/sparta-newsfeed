package com.sparta.spartanewsfeed.domain.member.service;

import static com.sparta.spartanewsfeed.domain.member.FriendStatus.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.FriendResponseDto;
import com.sparta.spartanewsfeed.domain.member.repository.FriendRepository;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;

@Service
public class FriendService {

	private final FriendRepository friendRepository;
	private final MemberRepository memberRepository;

	public FriendService(FriendRepository friendRepository, MemberRepository memberRepository) {
		this.friendRepository = friendRepository;
		this.memberRepository = memberRepository;
	}

	public List<FriendResponseDto> listFriends(Member member) {

		List<FriendResponseDto> list = new ArrayList<>();

		list.add(friendRepository.findByRequestMemberAndStatus(member, ACCEPT));
		list.add(friendRepository.findByResponseMemberAndStatus(member, ACCEPT));

		return list;
	}

	public void addFriend(Member member, Long friendId) {
		Member friend = memberRepository.findById(friendId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		Friend friend1 = Friend.builder().requestMember(member).responseMember(friend).status(PENDING).build();
		friendRepository.save(friend1);
	}

	public void acceptFriend(Member member, Long friendId) {
		Member friend = memberRepository.findById(friendId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		Friend friend1 = friendRepository.findByRequestMemberAndResponseMember(friend, member);
		friend1.update(ACCEPT);
	}

	public void deleteFriend(Member member, Long friendId) {
		Member friend = memberRepository.findById(friendId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		Friend friend1 = friendRepository.findByRequestMemberAndResponseMember(friend, member);
		friendRepository.delete(friend1);
	}

}
