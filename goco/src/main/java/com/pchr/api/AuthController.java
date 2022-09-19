package com.pchr.api;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
import com.pchr.dto.EmailAuthDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.dto.UnitDTO;
import com.pchr.service.impl.AuthServiceImpl;
import com.pchr.service.impl.EmpolyServiceImpl;
import com.pchr.service.impl.TokenDataImpl;
import com.pchr.service.impl.UnitServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthServiceImpl authService;
	private final EmpolyServiceImpl empolyServiceImpl;
	private final UnitServiceImpl unitImpl;
	private final TokenDataImpl tokenDataImpl;
	/**
	 * 회원가입
	 * 
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody EmployeeDTO employeeDTO, Errors errors) {
		if (errors.hasErrors()) {
			/* 유효성 통과 못한 필드와 메시지를 핸들링 */
			Map<String, String> validatorResult = AuthServiceImpl.validateHandling(errors);
			return ResponseEntity.ok(validatorResult);
		}
        return ResponseEntity.ok(authService.signup(employeeDTO));
	}  

	/**
	 * 로그인
	 * 
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EmployeeDTO employeeDTO, HttpServletResponse response) {
    	TokenDTO tokenDTO = authService.login(employeeDTO,response);
    	if(tokenDTO != null) {
    		tokenDataImpl.cookiesSave(response,tokenDTO,employeeDTO);
    	}
    	else{
    		throw new RuntimeException("토큰 생성 에러");
    	}
        return ResponseEntity.ok(tokenDTO);
    }
    
	/**
	 * ID or Email 기존 가입자 체크
	 * 
	 * @return boolean
	 */
    @GetMapping("checkinfo")
    public boolean checkInfo(@RequestParam String info) {
    	return empolyServiceImpl.idCheck(info);
    }  
    
	/**
	 * 회원가입시 이메일 인증번호 발송 코드
	 * 
	 * @return String
	 */
    @PostMapping("/sendemailforemail")
	public String sendEmailForEmail(@RequestBody EmployeeDTO e) {
		return authService.sendEmailForEmail(e.getEmail());
	} 

	/**
	 * ID 찾기, 이메일 인증번호 발송 코드
	 * 
	 * @return String
	 */
    @PostMapping("/sendemailforid")
    public String sendemail(@RequestBody EmployeeDTO e) {
    	return authService.sendEmailForId(e.getName(),e.getEmail());
    }  
    
	/**
	 * 비번 찾기, 이메일 인증번호 발송 코드
	 * 
	 * @return String
	 */
    @PostMapping("/sendemailforpwd") 
    public String findPassword(@RequestBody EmployeeDTO e) {
    	return authService.sendEmailForPwd(e.getEmpId(),e.getEmail());
    } 
    
	/**
	 * 인증 번호 확인 함수
	 * 
	 * @return ResponseEntity<?>
	 */
    @PostMapping("/find/{number}")
	public ResponseEntity<?> find(@PathVariable("number") int number, @RequestBody EmailAuthDTO emailAuthDTO) {
    	ResponseEntity<?> result = null;
		try {
			result = authService.find(number,emailAuthDTO.getEmail(),emailAuthDTO.getAuthenticationNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 

	/**
	 * 회원 가입시 부서 search code
	 * 
	 * @return List<UnitDTO>
	 */
	@GetMapping("/getallunit") 
	public List<UnitDTO> allUnit() {
		return unitImpl.unitAll();
	}
	
	/**
	 * 로그아웃
	 * 
	 * @return int
	 */
	@GetMapping("/logout")
	public int logOut(HttpServletRequest request,HttpServletResponse response) {
		return authService.logOut(request,response);
	}
	
}