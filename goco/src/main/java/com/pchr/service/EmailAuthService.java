package com.pchr.service;



public interface EmailAuthService {
	
	// 랜덤 번호를 생성해주면서 생성된 랜덤번호가 테이블에 있다면 지운다.
	public void makeRandomNumber();
	
	public String mailText(String email);
	public void mailSend(String setFrom, String toMail, String title, String content);
	public String save(String email);
	// 임시 비번 생성 및 고객에게 전송
	public String passwordText(String email);
}
