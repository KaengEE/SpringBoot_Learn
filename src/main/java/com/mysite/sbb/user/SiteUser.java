package com.mysite.sbb.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class SiteUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //기본키 자동증가 롱타입(int보다 큰 정수)
	
	@Column(unique = true) //중복 허용안됨
	private String username; 
	
	private String password;
	
	@Column(unique = true) //중복 허용안됨
	private String email;
	
}
