package com.sparta.spartanewsfeed.domain.member.controller;

import com.sparta.spartanewsfeed.domain.member.Member;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;
import com.sparta.spartanewsfeed.domain.member.dto.UpdateInfo;
import com.sparta.spartanewsfeed.domain.member.dto.UpdatePassword;
import com.sparta.spartanewsfeed.domain.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseMember findById(@PathVariable(name = "memberId") Long memberId) {
        return memberService.findById(memberId);
    }

    @PutMapping("/info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInfo(@RequestAttribute(name = "member") Member member,
                           @RequestBody @Valid UpdateInfo request) {
        memberService.updateInfo(member, request);
    }

    @PutMapping("/password")
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
}
