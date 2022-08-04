package com.pchr.service;

import com.pchr.dto.EmployeeRequestDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.TokenDTO;



public interface AuthService {    
    public EmployeeResponseDTO signup(EmployeeRequestDTO requestDto);

    public TokenDTO login(EmployeeRequestDTO requestDto);
    
    // 아이디 찾기위해 메일 보내는 함수 
	public String sendEmailForId(String name, String email);
	
	// 아이디 찾기 과정 중 인증 번호 확인하는 것
	public String findAuth(String authenticationNumber);
	
	// 비밀번호 찾기 위해 메일보내는 함수
	public String sendEmailForPwd(String id, String email);
	
	// 비밀번호 찾기 과정 중 인증 번호가 올바른지 확인하고 맞다면 임시 비밀번호 고객에게 전송 + db에 임시 비번 저장 
	public String findPassword(String authenticationNumber);

	public String idCheck(String info);
	
}
