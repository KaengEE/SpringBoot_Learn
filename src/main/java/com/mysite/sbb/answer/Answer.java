package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;

	//어떤질문에 대한 답변
	@ManyToOne
	private Question question;
	
	@ManyToOne
	private SiteUser author; //작성자
	
	private LocalDateTime modifyDate; //수정날짜
	
	//답변과 추천인(유저) 다대다 관계 n:n관계 한명의 유저는 1번 추천 (중복X -Set)
	@ManyToMany
	private Set<SiteUser> voter; //추천인

}
