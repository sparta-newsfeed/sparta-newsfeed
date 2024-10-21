package com.sparta.spartanewsfeed.domain.member;

import org.apache.catalina.User;

import com.sparta.spartanewsfeed.domain.Timestamp;
import com.sparta.spartanewsfeed.domain.member.dto.ResponseMember;
import com.sparta.spartanewsfeed.domain.member.dto.UpdateInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "MEMBER")
@AllArgsConstructor
@NoArgsConstructor
public class Member extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    // @Column(name = "ROLE", nullable = false)
    // @Enumerated(value = EnumType.STRING)
    // private UserRole role;

    public static ResponseMember makeResponse(Member member) {
        return ResponseMember.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                //.role(member.getRole().name())
                //.createdAt(member.getCreatedAt())
                //.updatedAt(member.getUpdatedAt())
                .build();
    }

    public void updateInfo(UpdateInfo request) {
        this.name = request.getName();
        this.nickname = request.getNickname();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public Member(String username, String nickname, String email, String password){
        this.name = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
