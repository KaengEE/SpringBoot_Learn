package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.sbb.question.DataNotFoundException;
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
	
	//답변할 정보조회
	public Answer getAnswer(int id) {
		Optional<Answer> answer = aRepo.findById(id);
		if(answer.isPresent()) {
			return answer.get();
		} else {
			throw new DataNotFoundException("answer not found");
		}
	}
	
	//답변수정
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.aRepo.save(answer);
	}
	
	//답변삭제
	public void delete(Answer answer) {
		this.aRepo.delete(answer);
	}
	
	//추천
	public void vote(Answer answer, SiteUser siteUser) {
		Set<SiteUser> list = answer.getVoter();
		boolean removeVote = false; //추천했는지 확인 -> 안했으면 false
		for(SiteUser user : list) {
			if(user.getUsername().equals(siteUser.getUsername())) {
				list.remove(user); //제거
				removeVote = true; //이미 추천됨
			}
		}
		if(!removeVote) {
			list.add(siteUser); //추천 안되어있으면 추가
		}
		
		this.aRepo.save(answer); //추천인 업데이트
	}
	
	
}
