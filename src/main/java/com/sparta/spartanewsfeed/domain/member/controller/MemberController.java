package com.sparta.spartanewsfeed.domain.member.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.OtherMemberProfile;
import com.sparta.spartanewsfeed.domain.member.dto.RequestVerifyIdentity;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;
import com.sparta.spartanewsfeed.domain.member.dto.UpdateInfo;
import com.sparta.spartanewsfeed.domain.member.dto.UpdatePassword;
import com.sparta.spartanewsfeed.domain.member.dto.VerifyIdentityResult;
import com.sparta.spartanewsfeed.domain.member.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseMember getLogInMemberInfo(@RequestAttribute(name = "member") Member member) {
		return ResponseMember.make(member);
	}

	@GetMapping("/{memberId}")
	@ResponseStatus(HttpStatus.OK)
	public OtherMemberProfile findById(@RequestAttribute(name = "member") Member member,
		@PathVariable(name = "memberId") Long memberId) {
		return memberService.findById(member, memberId);
	}

	@GetMapping("/search-condition")
	@ResponseStatus(HttpStatus.OK)
	public List<ResponseMember> findAllByNameOrNickname(
		@RequestParam(name = "nameOrNickname", defaultValue = "") String nameOrNickname,
		@RequestParam(name = "lastMemberId", defaultValue = "0") Long lastMemberId) {
		return memberService.findAllByNameOrNickname(nameOrNickname, lastMemberId);
	}

	@PostMapping("/verify-identity")
	@ResponseStatus(HttpStatus.OK)
	public VerifyIdentityResult verifyIdentity(@RequestBody @Valid RequestVerifyIdentity request,
		@RequestAttribute(name = "member") Member member) {
		return memberService.verifyIdentity(request, member);
	}

	@PatchMapping("/info")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateInfo(@RequestAttribute(name = "member") Member member,
		@RequestBody @Valid UpdateInfo request) {
		memberService.updateInfo(member, request);
	}

	@PatchMapping("/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updatePassword(@RequestAttribute(name = "member") Member member,
		@RequestBody @Valid UpdatePassword request) {
		memberService.updatePassword(member, request);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@RequestAttribute(name = "member") Member member) {
		memberService.delete(member);
	}

	@GetMapping("/search")
	public ResponseEntity<PagedModel<ResponseMember>> retrieveMembers(
		@RequestParam(value = "username", defaultValue = "") String username,
		@RequestAttribute("member") Member loginMember,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.retrieveFilteredFriendMembers(username, loginMember, pageable));
	}

	@GetMapping("/friend")
	public ResponseEntity<PagedModel<ResponseMember>> retrieveFriendMembers(
		@RequestAttribute("member") Member member,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.retrieveFriends(member, pageable));
	}

	@GetMapping("/wait-friends")
	public ResponseEntity<PagedModel<ResponseMember>> retrieveWaitFriendMembers(
		@RequestAttribute("member") Member member,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.findWaitFriendMembers(member, pageable));
	}

}
