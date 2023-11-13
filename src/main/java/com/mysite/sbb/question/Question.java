package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //기본키, 자동증가(.IDENTITY : auto increase)
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate; //날짜열
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList; //질문1개에 답변여러개
	
	@ManyToOne
	private SiteUser author; //작성자
	
	private LocalDateTime modifyDate; //수정날짜
	
	//질문과 추천인(유저) 다대다 관계 n:n관계 한명의 유저는 1번 추천 (중복X -Set)
	@ManyToMany
	private Set<SiteUser> voter; //추천인
}
