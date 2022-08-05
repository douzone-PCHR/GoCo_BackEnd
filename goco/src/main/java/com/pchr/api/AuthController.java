package com.pchr.api;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.pchr.dto.EmployeeDTO;

import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.entity.Authority;
import com.pchr.service.EmployeeService;
import com.pchr.service.impl.AuthServiceImpl;
import com.pchr.service.impl.EmpolyServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;
    private final EmpolyServiceImpl empolyServiceImpl;
    
  @PostMapping("/signup")
  public ResponseEntity<EmployeeResponseDTO> signup(@RequestBody EmployeeDTO employeeDTO) {
      return ResponseEntity.ok(authService.signup(employeeDTO));
  }  
// http://localhost:8080/auth/signup
//  {
//	   	"empId" : "ky",
//	      "password" : "1111",
//	  	"name" : "김용주",
//	      "phoneNumber" : "010-1234-4567",
//	  	"email" : "kyjdummy1@gmail.com",	
//	      "hiredate" : "1991-03-26T00:00:00",
//	      "jobTitle": {
//	        "jobTitleId" : "1"
//	       },
//	      "teamPosition": {
//	        "teamPositionId" : "1"
//	       },
//	       "unit":{
//	           "unitId" : "3"
//	       }
//	  }  

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(authService.login(employeeDTO));
    }
//http://localhost:8080/auth/login    
//    {
//     	"empId" : "kyj",
//        "password" : "1111"
//    } 
 
    @GetMapping("/sendEmailForId") // id 찾기위해 이메일 보내는함수
    public String sendemail(@RequestParam String name, @RequestParam String email) {
    	return authService.sendEmailForId(name,email);
    }   //http://localhost:8080/auth/sendEmailForId?name=김용주&email=kyjdummy@gmail.com
    
    @GetMapping("/findId") // 인증번호를 확인해서 인증번호가 맞다면 id를 반환해줌, 아이디찾기와 비번 찾기 모두 id를 반환해준다.
    public String findId(@RequestParam String authenticationNumber) {
    	return authService.findAuth(authenticationNumber);
    }  //http://localhost:8080/auth/findId?authenticationNumber=인증번호입력
    
    @GetMapping("/sendEmailForPwd") // 비번 찾기위해 이메일 보내는 함수 
    public String findPassword(@RequestParam String id, @RequestParam String email) {
    	return authService.sendEmailForPwd(id,email);
    }    //http://localhost:8080/auth/sendEmailForPwd?id=kyj&email=kyjdummy@gmail.com
    
    @GetMapping("/findPwd")// 임시 비번 email 발송 
    public String findPassword(@RequestParam String authenticationNumber) {
    	return authService.findPassword(authenticationNumber);
    }//	http://localhost:8080/auth/findPwd?authenticationNumber=인증번호입력
    
    @GetMapping("checkInfo") // 아이디와 이메일 이미 가입되어있는지 확인하는 것
    public String checkInfo(@RequestParam String info) {
    	return empolyServiceImpl.idCheck(info);
    }    // http://localhost:8080/auth/checkInfo?info=kyj


}