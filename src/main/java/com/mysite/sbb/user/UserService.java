package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.question.DataNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private PasswordEncoder pass; //시큐리티 설정에 Bean BCryptPasswordEncoder 주입
	
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		//비밀번호 암호화
		user.setPassword(pass.encode(password));
		//저장
		this.uRepo.save(user);
		return user;
	}
	
	//유저를 찾음
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = uRepo.findByusername(username);
		if(siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteuser not found");
		}
	}
}
