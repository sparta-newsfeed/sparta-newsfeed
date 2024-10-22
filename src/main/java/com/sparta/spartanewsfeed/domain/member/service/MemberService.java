package com.sparta.spartanewsfeed.domain.member.service;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.repository.FriendRepository;
import com.sparta.spartanewsfeed.domain.jwt.config.PasswordEncoder;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.*;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public OtherMemberProfile findById(Member member, Long id) {
        Member foundMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID 를 가진 회원을 찾을 수 없습니다."));

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
            throw new IllegalArgumentException("새로운 비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.updatePassword(encodedPassword);
        memberRepository.save(member);
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }
}
