package com.sparta.spartanewsfeed.domain.member.service;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.FriendResponseDto;
import com.sparta.spartanewsfeed.domain.member.repository.FriendRepository;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import com.sparta.spartanewsfeed.exception.customException.MakeFriendException;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.spartanewsfeed.domain.member.FriendStatus.ACCEPT;
import static com.sparta.spartanewsfeed.domain.member.FriendStatus.PENDING;
import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.*;

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
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));

		Friend friend1 = findByRequestMemberAndResponseMember(member, friend);
		if (member.isUserIdEqual(friendId)) {
			throw new MakeFriendException(CAN_NOT_FRIEND_WITH_YOURSELF);
		}
		if (friend1 != null) {
			throw new MakeFriendException(ALREADY_FRIEND);
		}
		friend1 = Friend.builder().requestMember(member).responseMember(friend).status(PENDING)
			.requestedAt(LocalDateTime.now()).build();
		friendRepository.save(friend1);
	}

	@Transactional
	public void acceptFriend(Member member, Long friendId) {
		Member friend = memberRepository.findById(friendId)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));

		Friend friend1 = friendRepository.findByRequestMemberAndResponseMember(friend, member);
		friend1.update(ACCEPT);
	}

	public void deleteFriend(Member member, Long friendId) {
		Member friend = memberRepository.findById(friendId)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));

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

	public List<Member> getRelatedFriends(Member requestMember) {
		List<Friend> friends = friendRepository.findFriendsByMember(requestMember);
		List<Member> members = new ArrayList<>(friends.stream()
			.map(friend -> friend.findFriend(requestMember))
			.flatMap(Optional::stream)
			.toList());
		members.add(requestMember);
		return members;
	}
}
