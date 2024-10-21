package com.sparta.spartanewsfeed.domain.member.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.FriendReponseDto;
import com.sparta.spartanewsfeed.domain.member.repository.FriendRepository;

@Service
public class FriendService {

	private final FriendRepository friendRepository;

	public FriendService(FriendRepository friendRepository) {
		this.friendRepository = friendRepository;
	}

	public List<FriendReponseDto> listFriends(Member member) {

		List<FriendReponseDto> list = new ArrayList<>();

		list.add(friendRepository.findByRequestMember(member));
		list.add(friendRepository.findByResponseMember(member));

		return list;
	}

	public Friend addFriend(Member member, String friendId) {
		return null;
	}

	public void deleteFriend(Member member, String friendId) {

	}

}
