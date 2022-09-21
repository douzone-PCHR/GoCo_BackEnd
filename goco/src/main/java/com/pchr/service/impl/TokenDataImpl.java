package com.pchr.service.impl;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.CookieGenerator;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.dto.TokenDataDTO;
import com.pchr.jwt.TokenProvider;
import com.pchr.repository.TokenDataRepository;
import com.pchr.response.Message;
import com.pchr.response.StatusEnum;
import com.pchr.service.TokenData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenDataImpl implements TokenData {
	private final  TokenDataRepository tokenDataRepository;
	private final TokenProvider tokenProvider;
	private final EmpolyServiceImpl empolyServiceImpl;
	Random random = new Random();
	//로그인시 access , refresh 토큰 넣는 것
	@Override
	public void insertCookies(HttpServletResponse response, String access , String refresh) {
		saveCookieAccessToken(response,access); // 쿠키에 저장
		saveCookieRefreshToken(response,refresh,60*60*24*7); // 쿠키에 저장
	}
	// access토큰 쿠키 저장 
	@Override
	public void saveCookieAccessToken(HttpServletResponse response, String accessToken) {
		System.out.println("엑세스 : "+accessToken);
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("accessToken");
		cg.setCookieMaxAge(4 * 60 * 60); // 60초 * 30분
		cg.setCookieSecure(true); // https로만 통신할 때만 쿠키를 전송한다는 옵션
		cg.addCookie(response, accessToken);
	}
	// refresh 토큰 쿠키 저장 
	@Override
	public void saveCookieRefreshToken(HttpServletResponse response, String refreshToken, int time) {
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("refreshToken");
		cg.setCookieSecure(true); // https로만 통신할 때만 쿠키를 전송한다는 옵션
		cg.setCookieHttpOnly(true);// 보안을위해 http-only 사용
		cg.setCookieMaxAge(time); // 60초 * 60분 * 24시간 * 7일
		cg.addCookie(response, refreshToken);
	}

	//로그인시 db에 토큰 저장하는 것
	@Override
	public void saveTokens(String accessToken, String refreshToken, String empId) {
		tokenDataRepository.deleteByAccessToken(accessToken);//기존 토큰 삭제 
		tokenDataRepository.deleteByRefreshToken(refreshToken);//기존 토큰 삭제
		tokenDataRepository.deleteByEmpId(empId);// 기존 id로 db삭제 
		TokenDataDTO tokenDataDTO = new TokenDataDTO();
		tokenDataDTO.setAccessToken(accessToken);
		tokenDataDTO.setRefreshToken(refreshToken);
		tokenDataDTO.setEmpId(empId);
		tokenDataDTO.setExpireTime(new Date((new Date()).getTime() + 604800 * 1000L));
		tokenDataRepository.save(tokenDataDTO.toEntity(tokenDataDTO));		
	}
	@Override
	public ResponseEntity<?> newToken(Cookie refreshToken, HttpServletResponse response) {
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		if(refreshToken==null) // 리프레쉬토큰을 못불러올경우 에러임
		{
			message.setStatus(StatusEnum.BAD_REQUEST);
			message.setMessage("Refresh 토큰을 불러올 수 없습니다.");
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
		}
		TokenDataDTO tokenDataDTO = tokenDataRepository.findByRefreshToken(refreshToken.getValue())
												.map(token-> token.toDTO(token))
												.orElseThrow(() -> new RuntimeException("403"));
		// 토큰유효시간 지났나 안지났나 확인
		tokenProvider.validateToken(response,tokenDataDTO.getRefreshToken());
		
//		emp에서 auth를 꺼낸 후 토큰과 쿠키를 각각 넣는다.
		EmployeeDTO empDTO = empolyServiceImpl.findByEmpId(tokenDataDTO.getEmpId())
												.map(emp->emp.toDTO(emp))
												.orElseThrow(() -> new RuntimeException("저장된 유저 데이터가 없습니다."));
		long randumNumber = random.nextInt()%30000; // -30초부터 +30초까지 사이랜덤 값생성으로 리프레쉬토큰의 값을 변경함
		long timeDiff = tokenDataDTO.getExpireTime().getTime()-(new Date()).getTime()+randumNumber;// 밀리세컨드
		tokenDataDTO.setAccessToken(tokenProvider.createAccessToken(empDTO.getEmpId(), String.valueOf(empDTO.getAuthority())));
		tokenDataDTO.setRefreshToken(tokenProvider.createRefreshToken(empDTO.getEmpId(), String.valueOf(empDTO.getAuthority()), new Date((new Date().getTime())+timeDiff)));
		saveCookieAccessToken(response,tokenDataDTO.getAccessToken());
		saveCookieRefreshToken(response,tokenDataDTO.getRefreshToken(),Long.valueOf(timeDiff/1000).intValue());//쿠키는 나누기 1000을 해줘야 1초가됨
		tokenDataRepository.save(tokenDataDTO.toEntity(tokenDataDTO));
		message.setStatus(StatusEnum.OK);
		message.setMessage("재발송 완료");
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
		}

	@Override
	@Scheduled(cron = "0 */5 * * * *")//5분마다실행
	//@Scheduled(fixedDelay=10000) // 1초마다실행
	public void deleteData() {
		tokenDataRepository.findAll().forEach(e->{
			if(e.getExpireTime().before(new Date())) {// 만료시간이 지난 것들 삭제 
				tokenDataRepository.deleteByEmpId(e.getEmpId());
			}
		});
	}
	@Override
	public boolean existsByRefreshToken(String refreshToken) {
		return tokenDataRepository.existsByRefreshToken(refreshToken);
	}
	@Override
	public boolean existsByAccessToken(String accessToken) {
		return tokenDataRepository.existsByAccessToken(accessToken);
	}
	@Override
	public int deleteByAccessToken(String accessToken) {
		return tokenDataRepository.deleteByAccessToken(accessToken);
	}
	@Override
	public int deleteByRefreshToken(String refreshToken) {
		return tokenDataRepository.deleteByRefreshToken(refreshToken);
	}
	@Override
	public void cookiesSave(HttpServletResponse response, TokenDTO tokenDTO, EmployeeDTO employeeDTO) {
		insertCookies(response,tokenDTO.getAccessToken(),tokenDTO.getRefreshToken()); // 브라우저 쿠키에 쿠키저장
		saveTokens(tokenDTO.getAccessToken(),tokenDTO.getRefreshToken(),employeeDTO.getEmpId());// db에 저장하는 것
		tokenDTO.setRefreshToken("");
	}	
}
