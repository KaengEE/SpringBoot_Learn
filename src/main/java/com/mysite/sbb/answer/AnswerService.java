package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

@Service
public class AnswerService {
	
	@Autowired
	private AnswerRepositoty aRepo;
	
	//답변저장시 질문객체와 답변내용을 가지고 입력받아서 답변을 등록
	public void creat(Question question,String content,SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		this.aRepo.save(answer);
	}
}
