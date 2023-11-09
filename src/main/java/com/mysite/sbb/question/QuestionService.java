package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository qRepo;
	
	public List<Question> getList() {
		return this.qRepo.findAll(); //모든 질문리스트
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
	public void create(String subject, String content) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		this.qRepo.save(question);
	}
}
