package com.pchr.api;

import java.util.List;
import java.util.Map;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.CookieGenerator;

import com.pchr.dto.EmailAuthDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.dto.UnitDTO;
import com.pchr.service.impl.AuthServiceImpl;
import com.pchr.service.impl.EmpolyServiceImpl;
import com.pchr.service.impl.UnitServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthServiceImpl authService;
	private final EmpolyServiceImpl empolyServiceImpl;
	private final UnitServiceImpl unitImpl;
	@PostMapping("/signup") // 회원가입
	public ResponseEntity<?> signup(@Valid @RequestBody EmployeeDTO employeeDTO, Errors errors) {
		if (errors.hasErrors()) {
			/* 유효성 통과 못한 필드와 메시지를 핸들링 */
			Map<String, String> validatorResult = AuthServiceImpl.validateHandling(errors);
			return ResponseEntity.ok(validatorResult);
		}
        return ResponseEntity.ok(authService.signup(employeeDTO));
	}  

  @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody EmployeeDTO employeeDTO, HttpServletResponse response) {
    	TokenDTO tokenDTO = authService.login(employeeDTO);
    	if(tokenDTO != null) {
//    		Cookie cookie = new Cookie("accessToken", tokenDTO.getAccessToken());
//    		cookie.setMaxAge(60*60);
//    		response.addCookie(cookie);
    		CookieGenerator cg = new CookieGenerator();
    		cg.setCookieName("accessToken");
    		cg.setCookieMaxAge(60*60*24*30); // 60초 * 60분 * 24시간 * 30일
    		cg.addCookie(response, tokenDTO.getAccessToken());
    	}else {
    		throw new RuntimeException("토큰 생성 에러");
    	}
        return ResponseEntity.ok(tokenDTO);
    }
    
    @GetMapping("checkInfo") // 아이디와 이메일 이미 가입되어있는지 확인하는 것
    public boolean checkInfo(@RequestParam String info) {
    	return empolyServiceImpl.idCheck(info);
    }   
    @PostMapping("/sendEmailForEmail") // 회원가입시 이메일 인증을위해 이메일 보내는 부분
	public String sendEmailForEmail(@RequestBody EmployeeDTO e) {
		return authService.sendEmailForEmail(e.getEmail());
	} 

    @PostMapping("/sendEmailForId") // id 찾기위해 이메일 보내는함수
    public String sendemail(@RequestBody EmployeeDTO e) {
    	return authService.sendEmailForId(e.getName(),e.getEmail());
    }  
    
    @PostMapping("/sendEmailForPwd") // 비번 찾기위해 이메일 보내는 함수 
    public String findPassword(@RequestBody EmployeeDTO e) {
    	return authService.sendEmailForPwd(e.getEmpId(),e.getEmail());
    } 
    
    @PostMapping("/find/{number}") // 1 회원가입시 이메일 인증 번호확인 , 2 아이디찾기 인증번호 반환 , 3 비밀번호 인증번호 확인
	public String find(@PathVariable("number") int number, @RequestBody EmailAuthDTO emailAuthDTO) {
		return authService.find(number,emailAuthDTO.getEmail(),emailAuthDTO.getAuthenticationNumber());
	} 
    
	@GetMapping("/getAllUnit") // 회원 가입시 모든 유닛 받아오기 
	public List<UnitDTO> allUnit() {
		return unitImpl.unitAll();
	}

}