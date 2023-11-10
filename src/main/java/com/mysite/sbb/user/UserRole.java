package com.mysite.sbb.user;

import lombok.Getter;

@Getter
public enum UserRole {

	//로그인 유저들의 권한설정
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	UserRole(String value){
		this.value = value;
	}
	
	private String value;
}
