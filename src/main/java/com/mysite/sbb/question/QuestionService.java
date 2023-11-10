package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.user.SiteUser;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository qRepo;
	
	public Page<Question> getList(int page) {
		Pageable pageable = PageRequest.of(page, 10,Sort.by("createDate").descending());
		return this.qRepo.findAll(pageable); //모든 질문리스트
	}
	
	public Question getQuestion(int id) {
		Optional<Question> question = qRepo.findById(id);
		if(question.isPresent()) {
			return question.get();
		}else {
			//새 에러를 실행
			throw new DataNotFoundException("question not found");
		}
	}

	//질문등록
	public void create(String subject, String content, SiteUser siteUser) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		question.setAuthor(siteUser);
		this.qRepo.save(question);
	}
	
	//질문수정
	public void modify(Question question,String subject,String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.qRepo.save(question);
	}
	
}
