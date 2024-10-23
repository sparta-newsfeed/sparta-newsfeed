package com.sparta.spartanewsfeed.domain.member.service;

import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartanewsfeed.domain.jwt.config.PasswordEncoder;
import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.FriendStatus;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.OtherMemberProfile;
import com.sparta.spartanewsfeed.domain.member.dto.RequestVerifyIdentity;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;
import com.sparta.spartanewsfeed.domain.member.dto.UpdateInfo;
import com.sparta.spartanewsfeed.domain.member.dto.UpdatePassword;
import com.sparta.spartanewsfeed.domain.member.dto.VerifyIdentityResult;
import com.sparta.spartanewsfeed.domain.member.repository.FriendRepository;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;
import com.sparta.spartanewsfeed.exception.customException.NotMatchPasswordException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final FriendRepository friendRepository;
	private final FriendService friendService;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public OtherMemberProfile findById(Member member, Long id) {
		Member foundMember = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));

		List<Friend> foundFriend = friendRepository.findAll(member.getId(), id);
		boolean isFriend = !foundFriend.isEmpty();

		return OtherMemberProfile.builder()
			.responseMember(ResponseMember.make(foundMember))
			.isFriend(isFriend)
			.build();
	}

	@Transactional(readOnly = true)
	public List<ResponseMember> findAllByNameOrNickname(String nameOrNickname, Long lastMemberId) {
		List<Member> foundMembers = memberRepository.findAll(nameOrNickname, lastMemberId);

		return foundMembers.stream().map(ResponseMember::make).toList();
	}

	public VerifyIdentityResult verifyIdentity(RequestVerifyIdentity request, Member member) {
		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			return new VerifyIdentityResult(false);
		}

		return new VerifyIdentityResult(true);
	}

	public void updateInfo(Member member, UpdateInfo request) {
		member.updateInfo(request);
		memberRepository.save(member);
	}

	@Transactional
	public void updatePassword(Member member, UpdatePassword request) {
		if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
			throw new NotMatchPasswordException(NOT_MATCH_CHECK_PASSWORD);
		}

		if (passwordEncoder.matches(request.getNewPassword(), member.getPassword())) {
			throw new NotMatchPasswordException(CURRENT_PASSWORD_AND_CHANGE_PASSWORD_IS_SAME);
		}

		String encodedPassword = passwordEncoder.encode(request.getNewPassword());
		member.updatePassword(encodedPassword);
		memberRepository.save(member);
	}

	public void delete(Member member) {
		memberRepository.delete(member);
	}

	public PagedModel<ResponseMember> retrieveFilteredFriendMembers(
		String username,
		Member loginMember,
		Pageable pageable
	) {
		List<Friend> friends = friendRepository.findFriendsByMember(loginMember);
		List<Member> friendMembers = findFriends(friends, loginMember);
		return new PagedModel<>(memberRepository.findFilteredMembers(friendMembers, loginMember, username, pageable)
			.map(ResponseMember::make));
	}

	public PagedModel<ResponseMember> retrieveFriends(Member loginMember, Pageable pageable) {
		List<Friend> friends = friendRepository.findFriendByMemberAndStatus(loginMember, FriendStatus.ACCEPT);
		List<Member> friendMembers = findFriends(friends, loginMember);
		return new PagedModel<>(
			new PageImpl<>(friendMembers.stream()
				.map(ResponseMember::make)
				.toList(), pageable, friendMembers.size())
		);
	}

	public PagedModel<ResponseMember> findWaitFriendMembers(Member loginMember, Pageable pageable) {
		List<Friend> friends = friendRepository.findRequestFriendByMemberAndStatus(loginMember, FriendStatus.PENDING);
		List<Member> requestFriends = findFriends(friends, loginMember);
		Page<ResponseMember> pagingMembers = new PageImpl<>(requestFriends.stream()
			.map(ResponseMember::make)
			.toList(), pageable, requestFriends.size());
		return new PagedModel<>(pagingMembers);
	}

	private List<Member> findFriends(List<Friend> friends, Member loginMember) {
		return friends.stream()
			.map(friend -> friend.findFriend(loginMember))
			.flatMap(Optional::stream)
			.toList();
	}
}
