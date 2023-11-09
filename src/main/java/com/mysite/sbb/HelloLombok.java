package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class HelloLombok {
	
	private final String hello;
	private final int lombok;
	
	public static void main(String[] args) {
		HelloLombok hello = new HelloLombok("헬로우",5);
		HelloLombok hello2 = new HelloLombok("헬2",10);
		
		System.out.println(hello2);
		System.out.println(hello);
	}
}
