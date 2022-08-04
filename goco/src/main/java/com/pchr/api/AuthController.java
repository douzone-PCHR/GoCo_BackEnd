package com.pchr.api;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.EmployeeRequestDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    
    @PostMapping("/signup")
    public ResponseEntity<EmployeeResponseDTO> signup(@RequestBody EmployeeRequestDTO requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }
// http://localhost:8080/auth/signup
//    {
//     	"empId" : "kyj",
//        "password" : "test12341",
//    	"name" : "김용주",
//        "phoneNumber" : "010-1234-4567",
//    	"email" : "test@test.com",	
//        "hiredate" : "1991-03-26T00:00:00",
//        "jobTitle": {
//          "jobTitleName" : "잡타이틀"
//         },
//        "teamPosition": {
//          "teampPositionName" : "팀포지션"
//         }
//    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody EmployeeRequestDTO requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
//http://localhost:8080/auth/login    
//    {
//     	"empId" : "kyj",
//        "password" : "test12341"
//    } 
 
    @GetMapping("/sendEmailForId") // id 찾기위해 이메일 보내는함수
    public String sendemail(@RequestParam String name, @RequestParam String email) {
    	return authService.sendEmailForId(name,email);
    }
    //http://localhost:8080/auth/sendEmailForId?name=김용주&email=yongj326@naver.com
    
    
    @GetMapping("/findAuth") // 인증번호를 확인해서 인증번호가 맞다면 id를 반환해줌, 아이디찾기와 비번 찾기 모두 id를 반환해준다.
    public String findId(@RequestParam String authenticationNumber) {
    	return authService.findAuth(authenticationNumber);
    }    
    //http://localhost:8080/auth/findAuth?authenticationNumber=인증번호입력
    
    @GetMapping("/sendEmailForPwd") // 비번 찾기위해 이메일 보내는 함수 
    public String findPassword(@RequestParam String id, @RequestParam String email) {
    	return authService.sendEmailForPwd(id,email);
    }
    //http://localhost:8080/auth/sendEmailForPwd?id=KYJempId&email=yongj326@naver.com
    
    @GetMapping("/findPwd")
    public String findPassword(@RequestParam String authenticationNumber) {
    	return authService.findPassword(authenticationNumber);
    }
//	http://localhost:8080/auth/findPwd?authenticationNumber=인증번호입력
}