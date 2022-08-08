package com.pchr.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


import com.pchr.dto.EmployeeDTO;

import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.TokenDTO;

import com.pchr.entity.EmailAuth;
import com.pchr.entity.Employee;
import com.pchr.jwt.TokenProvider;
import com.pchr.repository.EmailAuthRepository;


import com.pchr.service.AuthService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
//실제 로직들이 수행되는 장소
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManagerBuilder managerBuilder;
    private final EmpolyServiceImpl empolyServiceImpl;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailAuthServiceImpl emailAuthService; // 메일보내는 함수
	
    
//회원 가입시 오류 있으면 반환해주는 것
	public static Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
        	String validKeyName = String.format("valid_%s", error.getField());
        	validatorResult.put(validKeyName, error.getDefaultMessage());
        	}
        return validatorResult;
	}
    
  @Override
  public EmployeeResponseDTO signup(EmployeeDTO employeeDTO) {
	// id와 이메일이 이미 있으면 가입된 유저 반환
    if (empolyServiceImpl.existsByEmail(employeeDTO.getEmail()) | empolyServiceImpl.existsByEmpId(employeeDTO.getEmpId())  ) {
        throw new RuntimeException("이미 가입되어 있는 유저입니다");
    }
    Employee employee = employeeDTO.toEmpSignUp(passwordEncoder);
    return EmployeeResponseDTO.of(empolyServiceImpl.save(employee));
}	
	@Override
    public TokenDTO login(EmployeeDTO employeeDTO) {
//1) login 메소드는EmployeeRequestDTO에 있는 메소드 toAuthentication를 통해 생긴 UsernamePasswordAuthenticationToken 타입의 데이터를 가지게된다.
        UsernamePasswordAuthenticationToken authenticationToken = employeeDTO.toAuthentication();
        
//2) 주입받은 Builder를 통해 AuthenticationManager를 구현한 ProviderManager를 생성한다.
//3) 이후 ProviderManager는 데이터를 AbstractUserDetailsAuthenticationProvider 의 자식 클래스인 DaoAuthenticationProvider 를 주입받아서 호출한다.

// 4) DaoAuthenticationProvider 내부에 있는 authenticate에서 retrieveUser을 통해 DB에서의 User의 비밀번호가 실제 비밀번호가 맞는지 비교한다.
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
//5) retrieveUser에서는 DB에서의 User를 꺼내기 위해, CustomUserDetailService에 있는 loadUserByUsername을 가져와 사용한다.
        
        return tokenProvider.generateTokenDto(authentication);
    }	

	@Override
    // 아이디 찾기위해 메일 보내는 함수 
	public String sendEmailForId(String name, String email) {
		if(empolyServiceImpl.findByNameAndEmail(name,email)==null) { // 이름과 이메일로 해당하는 emp테이블을 가져와 사용자가 없다면 null을 반환하므로 조건문을 준다.
			return "사용자가 없습니다. 이메일 혹은 이름을 확인하세요 ";
		}		
		return emailAuthService.save(email);
	}	
	
	@Override
	// 아이디 찾기 과정 중 인증 번호 확인하는 것
	public String findAuth(String authenticationNumber) {
		EmailAuth emailAuth = emailAuthRepository.findByAuthenticationNumber(authenticationNumber);// 인증 번호로 테이블을 불러온다.
		if(emailAuth==null) {
			return "올바른 인증번호를 입력하세요";
		}
		else if(authenticationNumber.equals(emailAuth.getAuthenticationNumber())){// 사용자가 보낸 인증번호와 데이터에 있는 인증 번호가 같으면 실행	
			if(LocalDateTime.now().compareTo(emailAuth.getValidTime())<0) {//현재시간과 만료 시간 비교 
				return empolyServiceImpl.findByEmail(emailAuth.getEmail()).get().getEmpId(); // id값 반환 
			}else {
				return "시간 초과 다시 인증 바랍니다.";
			}
		}else {
			return "-1"; // -1이면 에러인것임
		}
	}	
	@Override
	// 비밀번호 찾기 위해 메일보내는 함수
	public String sendEmailForPwd(String id, String email) {
		if(empolyServiceImpl.findByEmpIdAndEmail(id,email)==null) { // 이름과 이메일로 해당하는 emp테이블을 가져와 사용자가 없다면 null을 반환하므로 조건문을 준다.
			return "사용자가 없습니다. 이메일 혹은 아이디를 확인하세요 ";
		}
		return emailAuthService.save(email);
	}	
	
	@Override
	// 비밀번호 찾기 과정 중 인증 번호가 올바른지 확인하고 맞다면 임시 비밀번호 고객에게 전송 + db에 임시 비번 저장 
	public String findPassword(String authenticationNumber) {
		EmailAuth emailAuth = emailAuthRepository.findByAuthenticationNumber(authenticationNumber);// 인증 번호로 테이블을 불러온다.
		if(emailAuth==null) {
			return "올바른 인증번호를 입력하세요";
		}
		else if(authenticationNumber.equals(emailAuth.getAuthenticationNumber())){	
			if(LocalDateTime.now().compareTo(emailAuth.getValidTime())<0) {// 시간 비교해서 유효할 경우 실행됨
				// 1) 랜덤 함수로 임시 비번을 생성하고 고객에게 임시 비번을 전송한다.
				String password = emailAuthService.passwordText(emailAuth.getEmail());//임시 패스워드 문자열 발행
				// 2) employee를 employeeDto로 바꾸고  employeeDto에 임시비번 저장하고 이걸다시 employee로 바꾸고, 이걸 레포를써서 저장한다.
				EmployeeDTO employeeDTO = empolyServiceImpl.findByEmail(emailAuth.getEmail())
											.map(emp->emp.toDTO(emp))
											.orElseThrow(()->new RuntimeException("유저 정보가 없습니다"));
				
				employeeDTO.setPassword(passwordEncoder.encode((password)));
				empolyServiceImpl.save(employeeDTO.toEntity(employeeDTO));
				return "이메일 발송";
			}else {
				return "시간 초과 다시 인증 바랍니다.";
			}
		}else {
			return "에러";
		}
	}

}
