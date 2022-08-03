package com.pchr.service;

import java.time.LocalDateTime;
import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.entity.EmailAuth;

import com.pchr.repository.EmailAuthRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailAuthService {
	@Autowired
	private JavaMailSenderImpl mailSender;
	private String authNum;
	private final EmailAuthRepository emailAuthRepository;
	
	public void makeRandomNumber() { // 랜덤 번호를 생성해주면서 생성된 랜덤번호가 테이블에 있다면 지운다.
		Random random = new Random();
		authNum = String.valueOf(random.nextInt(999999));
	}
	
	public String mailText(String email) {
		makeRandomNumber();
		String setFrom = "emailprojectkyj@gmail.com";// 보내는 사람
		String toMail = email;   // 받는사람
		String title = "회원 인증 이메일 입니다.";// 제목
        String content =        //메일보낼때 안의 내용        
                System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "안녕하세요 회원님 저희 홈페이지를 찾아주셔서 감사합니다"
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "비밀번호 찾기 인증번호는 " +authNum+ " 입니다. "
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "받으신 인증번호를 홈페이지에 입력해 주시면 다음으로 넘어갑니다.";   
        mailSend(setFrom, toMail, title, content);
        return String.valueOf(authNum);
	}
	public void mailSend(String setFrom, String toMail, String title, String content) { 
		try {
			MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            
            messageHelper.setFrom(setFrom); // 보내는사람 생략하면 정상작동을 안함
            messageHelper.setTo(toMail); // 받는사람 이메일
            messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
            messageHelper.setText(content); // 메일 내용
            
            mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String save(String email) {
		String authNum = mailText(email);// 메일보내는 부분, 메일을 보낸 후 인증 번호인 authNum 를 반환 받는다.
		if(emailAuthRepository.existsByAuthenticationNumber(authNum)) {// 반환받은 인증번호가 이미 테이블에 있는 경우 그 테이블을 지워준다.
			emailAuthRepository.deleteByAuthenticationNumber(authNum);
		}
		EmailAuth e = new EmailAuth(email,authNum,LocalDateTime.now().plusMinutes(5));// 인증 데이터를 저장하기 위해 EmailAuth겍체 생성, 유효시간은 현재보다 5분 앞으로함
		emailAuthRepository.save(e);
		return "메일이 전송 되었습니다.";
	}
}
