package com.pchr.service;

import java.util.List;

import com.pchr.entity.EmailAuth;

public interface EmailAuthService {
	
	// 랜덤 번호를 생성해주면서 생성된 랜덤번호가 테이블에 있다면 지운다.
	public void makeRandomNumber();
	
	public String mailText(String email);
	public void mailSend(String setFrom, String toMail, String title, String content);
	public String save(String email);
	// 임시 비번 생성 및 고객에게 전송
	public String passwordText(String email);
	// 새벽 4시마다 인증 테이블 최적화
	public void deleteData();

	public List<EmailAuth> findAll();
	public EmailAuth findByAuthenticationNumber(String authenticationNumber);
	public EmailAuth findByEmail(String email);
	public EmailAuth findByEmailAndAuthenticationNumber(String email,String authenticationNumber);
	public boolean existsByAuthenticationNumber(String authenticationNumber);
	public int deleteByEmail(String email);
	public int deleteByAuthenticationNumber(String authenticationNumber);
	public EmailAuth save(EmailAuth emailAuth);
	
}
