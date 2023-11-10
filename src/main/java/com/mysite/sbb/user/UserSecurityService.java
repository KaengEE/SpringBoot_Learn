package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private UserRepository uRepo;
	
	//스프링시큐리티가 인증에 사용하는 메서드
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<SiteUser> _siteUser = this.uRepo.findByusername(username);
		if(_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		SiteUser siteUser = _siteUser.get();
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		//권한리스트에 이름이 admin과 같을 경우에만 "ROLE_ADMIN"을 주고 나머지는 유저권한
		if("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		//시큐리티가 유저를 찾아서 검증 후 있을 경우 새 유저(이름,패스워드,권한)를 생성한다.
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
	}

}
