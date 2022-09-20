package com.pchr.service.impl;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import com.pchr.dto.EmailAuthDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.JobTitleDTO;
import com.pchr.dto.TeamPositionDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.entity.Commute;
import com.pchr.entity.EmailAuth;
import com.pchr.entity.Employee;
import com.pchr.jwt.TokenProvider;
import com.pchr.repository.CommuteRepository;
import com.pchr.response.Message;
import com.pchr.response.StatusEnum;
import com.pchr.service.AuthService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
//실제 로직들이 수행되는 장소
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManagerBuilder managerBuilder;
    private final EmpolyServiceImpl empolyServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailAuthServiceImpl emailAuthServiceImpl;// 메일보내는 함수
    private final TokenDataImpl tokenDataImpl;
    private final CommuteRepository commuteRepository;
    private final RedisServiceImpl redisServiceImpl;
// static이라 오버라이드 안됨
    //회원 가입시 유효성 검사 중 오류 있으면 반환해주는 것
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
				 
	    if (empolyServiceImpl.existsByEmail(employeeDTO.getEmail()) | empolyServiceImpl.existsByEmpId(employeeDTO.getEmpId())  ) {
	        throw new RuntimeException("이미 가입되어 있는 유저입니다.");
	    }
	    // unit_id로 매니저의 emp_num을 받아옴
	    List<Employee> e = empolyServiceImpl.findByManager(1L, employeeDTO.getUnit().getUnitId());
	    if(e.size()==1) {
	    	employeeDTO.setManager2(e.get(0).toDTO(e.get(0)));//매니저 넣어주는 코드 
	    }
	    employeeDTO.setJobTitle(getJobTitleDTO());// 사원직급 자동지정
	    employeeDTO.setTeamPosition(getTeamPositionDTO());// 팀원으로 자동 지정 
	    
	    Employee employee = employeeDTO.toEmpSignUp(passwordEncoder);
	    Employee commuteEmployee = empolyServiceImpl.save(employee);
	    insertCommute(commuteEmployee);
	    return  EmployeeResponseDTO.of(commuteEmployee);
	}	
	@Override
	public void insertCommute(Employee commuteEmployee) {
	    Commute commute = Commute.builder()
	    		.clockIn(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
						LocalDateTime.now().getDayOfMonth(), 0, 0))
	    		.clockOut(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
						LocalDateTime.now().getDayOfMonth(), 0, 0))
	    		.commuteStatus("0")
	    		.employee(commuteEmployee)
	    		.commuteCheck(0)
	    		.build();
	    commuteRepository.save(commute);
	}
////////////////////////////////////////////////////////////////////////////////////////////
	// 로그인시 토큰 만드는것 
	@Override
    public TokenDTO login(EmployeeDTO employeeDTO,HttpServletResponse response) { 
		if(failLoginCheckNum(employeeDTO,response)) {
			return null;
		}
        UsernamePasswordAuthenticationToken authenticationToken = employeeDTO.toAuthentication();
        try {
        	Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        	redisServiceImpl.deleteRedisValue(employeeDTO.getEmpId());
        	return tokenProvider.generateTokenDto(authentication);
        }catch(Exception e) {
        	return failLogin(employeeDTO,response); 
        }
    }
	@Override
	public boolean failLoginCheckNum(EmployeeDTO employeeDTO,HttpServletResponse response) {
		String value = redisServiceImpl.getRedisValue(employeeDTO.getEmpId());//키값 가져오기
		if(value==null) {
			return false;
		}else if(Integer.parseInt(value)>=5) {
			response.addHeader("loginFail", "true");//5분간 금지될 때 true리턴
			return true;
		}
		return false;
	}

	@Override
	public TokenDTO failLogin(EmployeeDTO employeeDTO,HttpServletResponse response) {
		String value = redisServiceImpl.getRedisValue(employeeDTO.getEmpId());//키값 가져오기
		if(value==null) {
			redisServiceImpl.setRedisValue(employeeDTO.getEmpId(),"1");
		}else {
			redisServiceImpl.increment(employeeDTO.getEmpId());
		}
		response.addHeader("loginFail", "");
		return null;
	}

	@Override
    // 아이디 찾기위해 메일 보내는 함수 
	public String sendEmailForId(String name, String email) {
		if(empolyServiceImpl.findByNameAndEmail(name,email)==null) { // 이름과 이메일로 해당하는 emp테이블을 가져와 사용자가 없다면 null을 반환하므로 조건문을 준다.
			throw new RuntimeException("사용자가 없습니다. 이메일 혹은 이름을 확인하세요.");
		}		
		return emailAuthServiceImpl.save(email);
	}	

	@Override
	// 비밀번호 찾기 위해 메일보내는 함수
	public String sendEmailForPwd(String id, String email) {
		if(empolyServiceImpl.findByEmpIdAndEmail(id,email)==null) { // 이름과 이메일로 해당하는 emp테이블을 가져와 사용자가 없다면 null을 반환하므로 조건문을 준다.
			throw new RuntimeException("사용자가 없습니다. 이메일 혹은 아이디를 확인하세요.");
		}
		return emailAuthServiceImpl.save(email);
	}	
	@Override
	public String sendEmailForEmail(String email) {
		if(empolyServiceImpl.idCheck(email)) {
			return emailAuthServiceImpl.save(email);
		}
		throw new RuntimeException("이미 가입되어 있는 유저입니다.");
	}

	@Override// 1 회원가입시 이메일 인증 번호확인 , 2 아이디찾기 인증번호 반환 , 3 비밀번호 인증번호 확인
	public ResponseEntity<?> find(int number,String email,String authenticationNumber) {
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        int checkNum = count(email);
		if(checkNum==-1) {
			message.setStatus(StatusEnum.BAD_REQUEST);
			message.setMessage("인증 번호가 3회이상 잘못 입력되었습니다. 재인증 바랍니다.");
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
//			return "인증 번호가 3회이상 잘못 입력되었습니다. 재인증 바랍니다.";
		}else if(checkNum==0) {
			message.setStatus(StatusEnum.BAD_REQUEST);
			message.setMessage("해당 메일로 발송된 인증번호가 없습니다.");
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
		}
		
		EmailAuth emailAuth = emailAuthServiceImpl.findByEmailAndAuthenticationNumber(email,authenticationNumber);// 인증 번호로 테이블을 불러온다.
		if(emailAuth==null) {
			message.setStatus(StatusEnum.BAD_REQUEST);
			message.setMessage("올바른 인증번호를 입력하세요.");
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
//			return "올바른 인증번호를 입력하세요.";
		}
		if(authenticationNumber.equals(emailAuth.getAuthenticationNumber())){	
			if(LocalDateTime.now().compareTo(emailAuth.getValidTime())<0) {// 시간 비교해서 유효할 경우 실행됨
				switch(number) {
				case 1:
					message.setStatus(StatusEnum.OK);
					message.setMessage(authenticationNumber);
					return new ResponseEntity<>(message, headers, HttpStatus.OK);
//					return  authenticationNumber;
				case 2:
					message.setStatus(StatusEnum.OK);
					message.setMessage(empolyServiceImpl.findByEmail(emailAuth.getEmail()).get().getEmpId());
					return new ResponseEntity<>(message, headers, HttpStatus.OK);
//					return empolyServiceImpl.findByEmail(emailAuth.getEmail()).get().getEmpId(); // id값 반환
				case 3:
					// 1) 랜덤 함수로 임시 비번을 생성하고 고객에게 임시 비번을 전송한다.
					String password = emailAuthServiceImpl.passwordText(emailAuth.getEmail());//임시 패스워드 문자열 발행
					// 2) employee를 employeeDto로 바꾸고  employeeDto에 임시비번 저장하고 이걸다시 employee로 바꾸고, 이걸 레포를써서 저장한다.
					EmployeeDTO employeeDTO = empolyServiceImpl.findByEmail(emailAuth.getEmail())
												.map(emp->emp.toDTO(emp))
												.orElseThrow(()->new RuntimeException("유저 정보가 없습니다."));
					
					employeeDTO.setPassword(passwordEncoder.encode((password)));
					empolyServiceImpl.save(employeeDTO.toEntity(employeeDTO));
					// redis에 저장되어있는 empID 삭제
					redisServiceImpl.deleteRedisValue(employeeDTO.getEmpId());
					message.setStatus(StatusEnum.OK);
					message.setMessage("이메일로 비밀번호가 발송 되었습니다.");
					return new ResponseEntity<>(message, headers, HttpStatus.OK);
//					return "이메일로 비밀번호가 발송 되었습니다.";
				default :
					message.setStatus(StatusEnum.BAD_REQUEST);
					message.setMessage("잘못된 정보가 입력되었습니다.");
					return new ResponseEntity<>(message, headers, HttpStatus.OK);
				}
			}else {
				message.setStatus(StatusEnum.BAD_REQUEST);
				message.setMessage("시간 초과 다시 인증 바랍니다.");
				return new ResponseEntity<>(message, headers, HttpStatus.OK);
			}
		}
		message.setStatus(StatusEnum.BAD_REQUEST);
		message.setMessage("에러 발생");
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}
	
	@Override// 인증 번호 반환 해주는 서비스 코드 간결화를 위해 분할
	public int count(String email) {
		EmailAuth emailAuthThree = emailAuthServiceImpl.findByEmail(email);
		if(emailAuthThree==null) {
			return 0;
		}
		if(emailAuthThree.getCount()>3) {
			emailAuthServiceImpl.deleteByEmail(email);
			return -1;//인증을 조회한 횟수가 3회면 이메일 삭제
		}else { // emailAuth의 인증 번호의 count를 뽑아서 1을 증가 시키고 저장한다.			
			EmailAuthDTO emailAuthDTO  = emailAuthThree.toDTO(emailAuthThree);
			emailAuthDTO.setCount(emailAuthDTO.getCount()+1);
			emailAuthServiceImpl.save(emailAuthDTO.toEntity(emailAuthDTO));
		}
		return 1;
	}
	@Override// 회원 가입시 팀 포지션 팀원으로 자동 지정
	public TeamPositionDTO getTeamPositionDTO() {
		TeamPositionDTO team = new TeamPositionDTO();
	    team.setTeamPositionId(2L);
	    return team;
	}
	@Override// 회원 가입시 사원직급으로 자동 지정 
	public JobTitleDTO getJobTitleDTO() {
		JobTitleDTO job = new JobTitleDTO();
	    job.setJobTitleId(1L);
	    return job;
	}
	@Override
	public int logOut(HttpServletRequest request,HttpServletResponse response) {
		tokenDataImpl.deleteByAccessToken(tokenProvider.getAccessToken(request));
		tokenDataImpl.deleteByRefreshToken(tokenProvider.getRefreshToken(request));
		tokenProvider.cookieReset(response);
		return 1;
	}
	@Override
	public ResponseEntity<?> tokenError() {
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		message.setStatus(StatusEnum.BAD_REQUEST);
		message.setMessage("토큰 생성 에러");
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	public int deleteCookie(HttpServletResponse response) {
		tokenProvider.cookieReset(response);
		return 1;
	}
}
