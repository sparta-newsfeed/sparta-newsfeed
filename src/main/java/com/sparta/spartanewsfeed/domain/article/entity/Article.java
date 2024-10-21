package com.sparta.spartanewsfeed.domain.article.entity;

import com.sparta.spartanewsfeed.domain.Timestamp;
import com.sparta.spartanewsfeed.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class Article extends Timestamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String body;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member author;

	public void update(String title, String body) {
		if (title != null) {
			this.title = title;
		}

		if (body != null) {
			this.body = body;
		}
	}
}