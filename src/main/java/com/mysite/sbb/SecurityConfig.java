package com.mysite.sbb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.sbb.user.UserSecurityService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
 	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 		http.authorizeHttpRequests().antMatchers("/**").permitAll()
 		.and()
 			.csrf().ignoringAntMatchers("/h2-console/**")
 			.and()
 				.headers()
 				.addHeaderWriter(new XFrameOptionsHeaderWriter(
 						XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
 			.and()
 				.formLogin()
 				.loginPage("/user/login")
 				.defaultSuccessUrl("/")
 			.and()
 				.logout()
 				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
 				.logoutSuccessUrl("/")
 				.invalidateHttpSession(true);
 		
 		return http.build();
 	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
