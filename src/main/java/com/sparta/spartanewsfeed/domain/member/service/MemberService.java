package com.sparta.spartanewsfeed.domain.member.service;

import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;
import com.sparta.spartanewsfeed.domain.member.dto.UpdateInfo;
import com.sparta.spartanewsfeed.domain.member.dto.UpdatePassword;
import com.sparta.spartanewsfeed.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public ResponseMember findById(Long id) {
        Member foundMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID 를 가진 회원을 찾을 수 없습니다."));

        return Member.makeResponse(foundMember);
    }

    public void updateInfo(Member member, UpdateInfo request) {
        member.updateInfo(request);
        memberRepository.save(member);
    }

    @Transactional
    public void updatePassword(Member member, UpdatePassword request) {
        // 요청으로 전달받은 현재 비밀번호, 새 비밀번호, 새 비밀번호2(입력 확인)를 암호화한 뒤 회원의 비밀번호와 비교해야함
        // 현재 암호화 구현이 되어있지 않아 주석으로 남김

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("새로운 비밀번호가 일치하지 않습니다.");
        }

        if (!member.getPassword().equals(request.getOldPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        member.updatePassword(request.getNewPassword());
        memberRepository.save(member);
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }
}
