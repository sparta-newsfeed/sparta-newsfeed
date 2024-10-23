package com.sparta.spartanewsfeed.domain.member;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "request_id")
	private Member requestMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "response_id")
	private Member responseMember;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private FriendStatus status;

	@CreatedDate
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime requestedAt;

	public void update(FriendStatus friendStatus) {
		this.status = friendStatus;
	}

	public Optional<Member> findFriend(Member member) {
		if (requestMember.isUserIdEqual(member.getId())) {
			return Optional.ofNullable(responseMember);
		}
		if (responseMember.isUserIdEqual(member.getId())) {
			return Optional.ofNullable(requestMember);
		}

		return Optional.empty();
	}
}
