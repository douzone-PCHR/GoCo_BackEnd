package com.pchr.api;

import java.util.Map;

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

import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.service.impl.AuthServiceImpl;
import com.pchr.service.impl.EmpolyServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthServiceImpl authService;
	private final EmpolyServiceImpl empolyServiceImpl;

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
	public ResponseEntity<TokenDTO> login(@RequestBody EmployeeDTO employeeDTO) {
		return ResponseEntity.ok(authService.login(employeeDTO));
	}

	@GetMapping("checkInfo") // 아이디와 이메일 이미 가입되어있는지 확인하는 것
	public boolean checkInfo(@RequestParam String info) {
		return empolyServiceImpl.idCheck(info);
	} // http://localhost:8080/auth/checkInfo?info=kyj

	@GetMapping("/sendEmailForEmail") // 회원가입시 이메일 인증을위해 이메일 보내는 부분
	public String sendEmailForEmail(@RequestBody EmployeeDTO e) {
		return authService.sendEmailForEmail(e.getEmail());
	} // http://localhost:8080/auth/sendEmailForEmail

	@GetMapping("/sendEmailForId") // id 찾기위해 이메일 보내는함수
	public String sendemail(@RequestBody EmployeeDTO e) {
		return authService.sendEmailForId(e.getName(), e.getEmail());
	} // http://localhost:8080/auth/sendEmailForId

	@GetMapping("/sendEmailForPwd") // 비번 찾기위해 이메일 보내는 함수
	public String findPassword(@RequestBody EmployeeDTO e) {
		return authService.sendEmailForPwd(e.getEmpId(), e.getEmail());
	} // http://localhost:8080/auth/sendEmailForPwd

	@GetMapping("/find/{number}") // 1 회원가입시 이메일 인증 번호확인 , 2 아이디찾기 인증번호 반환 , 3 비밀번호 인증번호 확인
	public String find(@PathVariable("number") int number, @RequestParam String authenticationNumber) {
		return authService.find(number, authenticationNumber);
	} // http://localhost:8080/auth/find/1?authenticationNumber=

}