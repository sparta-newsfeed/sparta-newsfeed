package com.sparta.spartanewsfeed.domain.member.service;

import static com.sparta.spartanewsfeed.domain.member.FriendStatus.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.FriendResponseDto;
import com.sparta.spartanewsfeed.domain.member.repository.FriendRepository;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "FriendService")
public class FriendService {

	private final FriendRepository friendRepository;
	private final MemberRepository memberRepository;

	public FriendService(FriendRepository friendRepository, MemberRepository memberRepository) {
		this.friendRepository = friendRepository;
		this.memberRepository = memberRepository;
	}

	public Page<FriendResponseDto> listFriends(Member member, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("requestedAt").descending());

		// Page<Friend> friend = friendRepository.findByRequestMember(member, pageable);
		Page<Friend> friend = friendRepository.findByStatusAndRequestMemberOrResponseMember(ACCEPT, member, member,
			pageable);
		return friend.map(FriendResponseDto::new);
	}

	public void addFriend(Member member, Long friendId) {
		Member friend = memberRepository.findById(friendId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		Friend friend1 = Friend.builder()
			.requestMember(member)
			.responseMember(friend)
			.status(PENDING)
			.build();
		friendRepository.save(friend1);
	}

	@Transactional
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

	public List<Member> getRelatedFriends(Member requestMember) {
		List<Friend> friends = friendRepository.findFriendsByMember(requestMember)
			.orElse(List.of());

		return friends.stream()
			.map(friend -> friend.findFriend(requestMember))
			.flatMap(Optional::stream)
			.toList();
	}
}
