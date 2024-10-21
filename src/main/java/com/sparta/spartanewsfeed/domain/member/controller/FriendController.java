package com.sparta.spartanewsfeed.domain.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.FriendResponseDto;
import com.sparta.spartanewsfeed.domain.member.service.FriendService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

	private final FriendService friendService;

	public FriendController(final FriendService friendService) {
		this.friendService = friendService;
	}

	@GetMapping
	public List<FriendResponseDto> listFriends(HttpServletRequest request) {
		Member member = (Member)request.getAttribute("member");
		return friendService.listFriends(member);
	}

	@PostMapping("/{friendId}")
	public void addFriend(HttpServletRequest request, @PathVariable("friendId") Long friendId) {
		Member member = (Member)request.getAttribute("member");
		friendService.addFriend(member, friendId);
	}

	@PostMapping("/{friendId}")
	public void acceptFriend(HttpServletRequest request, @PathVariable("friendId") Long friendId) {
		Member member = (Member)request.getAttribute("member");
		friendService.acceptFriend(member, friendId);
	}

	@DeleteMapping("/{friendId}")
	public void deleteFriend(HttpServletRequest request, @PathVariable("friendId") Long friendId) {
		Member member = (Member)request.getAttribute("member");
		friendService.deleteFriend(member, friendId);
	}

}