package com.pchr.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.dto.EmployeeRequestDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.entity.EmailAuth;
import com.pchr.entity.Employee;
import com.pchr.jwt.TokenProvider;
import com.pchr.repository.EmailAuthRepository;
import com.pchr.repository.EmployeeRepository;
import com.pchr.repository.JobTitleRepository;
import com.pchr.repository.TeamPositionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
//실제 로직들이 수행되는 장소
public class AuthService {
	
    private final AuthenticationManagerBuilder managerBuilder;
    private final EmployeeRepository employeeRepository;
    private final JobTitleRepository jobTitleRepository;
    private final TeamPositionRepository teamPositionRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailAuthService emailAuthService; // 메일보내는 함수

    //signup 메소드는 평범하게 회원가입을 하는 메소드로, Spring Data JPA의 주요 로직으로 구성된다.
    public EmployeeResponseDTO signup(EmployeeRequestDTO requestDto) {
    	// id와 이메일이 이미 있으면 가입된 유저 반환
        if (employeeRepository.existsByEmail(requestDto.getEmail()) | employeeRepository.existsByEmpId(requestDto.getEmpId())  ) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        Employee employee = requestDto.toEmployee(passwordEncoder);
        // employee 저장 전에 jobtitle과 teamposition 테이블에 데이터를 먼저 저장한다
        jobTitleRepository.save(employee.getJobTitle());
        teamPositionRepository.save(employee.getTeamPosition());
        return EmployeeResponseDTO.of(employeeRepository.save(employee));
    }

    public TokenDTO login(EmployeeRequestDTO requestDto) {
//1) login 메소드는EmployeeRequestDTO에 있는 메소드 toAuthentication를 통해 생긴 UsernamePasswordAuthenticationToken 타입의 데이터를 가지게된다.
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        
//2) 주입받은 Builder를 통해 AuthenticationManager를 구현한 ProviderManager를 생성한다.
//3) 이후 ProviderManager는 데이터를 AbstractUserDetailsAuthenticationProvider 의 자식 클래스인 DaoAuthenticationProvider 를 주입받아서 호출한다.

// 4) DaoAuthenticationProvider 내부에 있는 authenticate에서 retrieveUser을 통해 DB에서의 User의 비밀번호가 실제 비밀번호가 맞는지 비교한다.
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
//5) retrieveUser에서는 DB에서의 User를 꺼내기 위해, CustomUserDetailService에 있는 loadUserByUsername을 가져와 사용한다.
        
        return tokenProvider.generateTokenDto(authentication);
    }

    
    // 아이디 찾기위해 메일 보내는 함수 
	public String sendEmailForId(String name, String email) {
		if(employeeRepository.findByNameAndEmail(name,email)==null) { // 이름과 이메일로 해당하는 emp테이블을 가져와 사용자가 없다면 null을 반환하므로 조건문을 준다.
			return "사용자가 없습니다. 이메일 혹은 이름을 확인하세요 ";
		}		
		return emailAuthService.save(email);
	}
	
	// 아이디 찾기 인증 번호 확인하는 것
	public String findAuth(String authenticationNumber) {
		EmailAuth emailAuth = emailAuthRepository.findByAuthenticationNumber(authenticationNumber);// 인증 번호로 테이블을 불러온다.
		if(emailAuth==null) {
			return "올바른 인증번호를 입력하세요";
		}
		else if(authenticationNumber.equals(emailAuth.getAuthenticationNumber())){// 사용자가 보낸 인증번호와 데이터에 있는 인증 번호가 같으면 실행	
			if(LocalDateTime.now().compareTo(emailAuth.getValidTime())<0) {//현재시간과 만료 시간 비교 
				String email = emailAuth.getEmail();
				Optional<Employee> employee = employeeRepository.findByEmail(email);
				return employee.get().getEmpId(); // id값 반환 
			}else {
				return "시간 초과 다시 인증 바랍니다.";
			}
		}else {
			return "-1"; // -1이면 에러인것임
		}
	}
	// 비밀번호 찾기 위해 메일보내는 함수
	public String sendEmailForPwd(String id, String email) {
		if(employeeRepository.findByEmpIdAndEmail(id,email)==null) { // 이름과 이메일로 해당하는 emp테이블을 가져와 사용자가 없다면 null을 반환하므로 조건문을 준다.
			return "사용자가 없습니다. 이메일 혹은 아이디를 확인하세요 ";
		}
		return emailAuthService.save(email);
	}
}
