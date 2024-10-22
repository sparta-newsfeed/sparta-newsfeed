package com.sparta.spartanewsfeed.domain.member;

import com.sparta.spartanewsfeed.domain.Timestamp;
import com.sparta.spartanewsfeed.domain.member.dto.UpdateInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	@Column(name = "ROLE", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRole role;

	public Member(String username, String nickname, UserRole role, String email, String password) {
		this.name = username;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public void updateInfo(UpdateInfo request) {
		this.nickname = request.getNickname();
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public boolean isUserIdEqual(Long id) {
		return id.equals(this.id);
	}

	public boolean isAdmin() {
		return role == UserRole.ADMIN;
	}
}
