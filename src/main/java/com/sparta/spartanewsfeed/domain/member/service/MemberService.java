package com.sparta.spartanewsfeed.domain.member.service;

import com.sparta.spartanewsfeed.domain.member.Friend;
import com.sparta.spartanewsfeed.domain.member.WithdrawnMember;
import com.sparta.spartanewsfeed.domain.member.repository.FriendRepository;
import com.sparta.spartanewsfeed.domain.jwt.config.PasswordEncoder;
import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.*;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;
import com.sparta.spartanewsfeed.exception.customException.NotMatchPasswordException;
import com.sparta.spartanewsfeed.exception.enums.ExceptionCode;
import com.sparta.spartanewsfeed.domain.member.repository.WithdrawnMemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.spartanewsfeed.exception.enums.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;
    private final WithdrawnMemberRepository withdrawnMemberRepository;

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
        WithdrawnMember withdrawnMember = new WithdrawnMember(member.getEmail());
        withdrawnMemberRepository.save(withdrawnMember);

        memberRepository.delete(member);
    }
}
