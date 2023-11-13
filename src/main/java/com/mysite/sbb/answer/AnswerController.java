package com.mysite.sbb.answer;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@Controller
@RequestMapping("/answer")
public class AnswerController {

	@Autowired
	private QuestionService qService;
	
	@Autowired
	private AnswerService aService;
	
	@Autowired
	private UserService uService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model,@PathVariable("id") int id,
			                   @Valid AnswerForm answerForm, BindingResult result,
			                   Principal principal) {
		//질문가져오기
		Question question = this.qService.getQuestion(id);
		
		if(result.hasErrors()) {
			model.addAttribute("question",question);
			return "question_detail";
		}
		
		SiteUser siteUser = uService.getUser(principal.getName());
		
		//답변저장
		this.aService.creat(question, answerForm.getContent(), siteUser);
		
		return String.format("redirect:/question/detail/%s", id);
	}

	
	//수정전
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modifyAnswer(AnswerForm answerForm, @PathVariable("id") int id,
							   Principal principal) {
		Answer answer = this.aService.getAnswer(id);
		//확인
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}
	
	//수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String modifyAnswer(@Valid AnswerForm answerForm, BindingResult result,
								@PathVariable("id") int id,  Principal principal) {
		if(result.hasErrors()) {
			return "answer_form";
		}
		
		//답변가져오기
		Answer answer = this.aService.getAnswer(id);
		//한번더 확인
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
		}
		
		//수정답변저장
		this.aService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId()); //질문의 id가져와야함
	}
	
	//삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") int id) {
		//답변가져오기
		Answer answer = this.aService.getAnswer(id);
		//한번더 확인
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
		}
		
		//삭제
		this.aService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId()); //질문의 id가져와야함
	}
	
	//답변추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.aService.getAnswer(id);
        SiteUser siteUser = this.uService.getUser(principal.getName());
        this.aService.vote(answer, siteUser);
        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }
	
}
