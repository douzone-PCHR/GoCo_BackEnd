package com.pchr.service;

import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.TokenDTO;



public interface AuthService {    
	// 회원가입
	public EmployeeResponseDTO signup(EmployeeDTO employeeDTO);
	// 회원가입시 이메일 인증번호 보내는 것
	public String sendEmailForEmail(String email);
	//로그인
	public TokenDTO login(EmployeeDTO employeeDTO);
    // 아이디 찾기위해 메일 보내는 함수 
	public String sendEmailForId(String name, String email);
	// 비밀번호 찾기 위해 메일보내는 함수
	public String sendEmailForPwd(String id, String email);
	// 1 회원가입시 이메일 인증 번호확인 , 2 아이디찾기 인증번호 반환 , 3 비밀번호 인증번호 확인
	public String find(int number,String email, String authenticationNumber);
}
