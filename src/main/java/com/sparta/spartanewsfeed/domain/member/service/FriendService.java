package com.sparta.spartanewsfeed.domain.member.service;

import static com.sparta.spartanewsfeed.domain.member.FriendStatus.*;

import java.time.LocalDateTime;

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

@Service
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

		Friend friend1 = findByRequestMemberAndResponseMember(member, friend);
		if (!member.getId().equals(friendId)) {
			throw new IllegalArgumentException("본인과 친구가 될 수 없습니다.");
		}
		if (friend1 != null) {
			throw new IllegalArgumentException("이미 친구입니다.");
		}
		friend1 = Friend.builder().requestMember(member).responseMember(friend).status(PENDING)
			.requestedAt(LocalDateTime.now())
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

		Friend friend1 = findByRequestMemberAndResponseMember(member, friend);

		friendRepository.delete(friend1);

	}

	private Friend findByRequestMemberAndResponseMember(Member member, Member friend) {
		Friend friend1 = friendRepository.findByRequestMemberAndResponseMember(member, friend);
		if (friend1 == null) {
			friend1 = friendRepository.findByRequestMemberAndResponseMember(member, friend);
		}
		return friend1;
	}

}
