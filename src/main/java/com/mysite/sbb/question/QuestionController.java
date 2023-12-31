package com.mysite.sbb.question;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@Controller
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	private QuestionService qService;
	
	@Autowired
	private UserService uService;
	
	@GetMapping("/list")
	public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {
		Page<Question> paging = qService.getList(page);
		model.addAttribute("paging", paging);
		
		return "question_list";
	}
	
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") int id,
			AnswerForm answerForm,@RequestParam("page") int page) {
		Question question = qService.getQuestion(id);
		Page<Question> paging = qService.getList(page);
		model.addAttribute("paging", paging);
		model.addAttribute("question", question);

		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {

		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult result,Principal principal) {
		
		if(result.hasErrors()) {
			return "question_form";
		}
		//작성자입력
		SiteUser siteUser = uService.getUser(principal.getName());
		
		//저장
		qService.create(questionForm.getSubject(),questionForm.getContent(),siteUser);
		
		return "redirect:/question/list";
	}
	
	//수정페이지
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}/{page}")
	public String questionModify(QuestionForm questionForm,
			                     @PathVariable("id") int id,@PathVariable("page") int page,
			                     Principal principal,Model model) {
		
		//질문가져오기
		Question question = this.qService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
		}
		
		//수정전 정보가져오기
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		model.addAttribute("modify", true); //제목 조건문
		return "question_form";
	}
	
	
	//질문수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}/{page}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult result,
			                     @PathVariable("id") int id,@PathVariable("page") int page,
			                     Principal principal) {
		
		if(result.hasErrors()) {
			return "question_form";
		}
		
		//질문가져오기
		Question question = this.qService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
		}
				
		this.qService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s?page=%s", id, page);
	}
	
	//질문삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") int id) {
		Question question = this.qService.getQuestion(id);
		//한번 더 확인
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        //삭제
        this.qService.delete(question);
        
        return "redirect:/";

	}
	
	//질문 추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}/{page}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id,Model model,@PathVariable("page") int page) {
        Question question = this.qService.getQuestion(id);
        SiteUser siteUser = this.uService.getUser(principal.getName());
        
        this.qService.vote(question, siteUser);
        //return String.format("redirect:/question/detail/%s", id);
        return "redirect:/question/detail/" + id +"?page=" + page;
    }
	
  
	
}
