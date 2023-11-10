package com.mysite.sbb.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService uService;
	
	//회원가입
	@GetMapping("/signup")
	public String singup(UserCreateForm userCreatForm) {
		return "signup_form";
	}
	
	//회원가입
	@PostMapping("/signup")
	public String singup(@Valid UserCreateForm userCreatForm , BindingResult result) {
		if(result.hasErrors()) {
			return "signup_form";
		}
		
		if(!userCreatForm.getPassword1().equals(userCreatForm.getPassword2())) {
			result.rejectValue("password2", "passwordInCorrect","패스워드가 일치하지 않습니다.");
			return "signup_form";
		}
		
		try {
			uService.create(userCreatForm.getUsername(), userCreatForm.getEmail(), userCreatForm.getPassword1());
			
		} catch (DataIntegrityViolationException e) {
			result.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "signup_form";
		} catch (Exception e) {
			//그 외 모든 에러
			e.printStackTrace(); //콘솔에 에러메시지
			result.reject("signupFailed", "예상치 못한 에러가 발생했습니다.");
		}
		
		
		return "redirect:/";
	}
	
	//로그인 post는 시큐리티가 처리함
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
	
}
