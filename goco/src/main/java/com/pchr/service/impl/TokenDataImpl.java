package com.pchr.service.impl;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.CookieGenerator;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.TokenDataDTO;
import com.pchr.jwt.TokenProvider;
import com.pchr.repository.TokenDataRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenDataImpl {
	private final  TokenDataRepository tokenDataRepository;
	private final TokenProvider tokenProvider;
	private final EmpolyServiceImpl empolyServiceImpl;
	Random random = new Random();
	//로그인시 access , refresh 토큰 넣는 것 
	public void insertCookies(HttpServletResponse response, String access , String refresh) {
		saveCookieAccessToken(response,access); // 쿠키에 저장
		saveCookieRefreshToken(response,refresh,60*60*24*7); // 쿠키에 저장
	}
	// access토큰 쿠키 저장 
	private void saveCookieAccessToken(HttpServletResponse response, String accessToken) {
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("accessToken");
		cg.setCookieMaxAge(60*30); // 60초 * 30분
		cg.setCookieSecure(true); // https로만 통신할 때만 쿠키를 전송한다는 옵션
		cg.addCookie(response, accessToken);
	}
	// refresh 토큰 쿠키 저장 
	private void saveCookieRefreshToken(HttpServletResponse response, String refreshToken, int time) {
		CookieGenerator cg2 = new CookieGenerator();
		cg2.setCookieName("refreshToken");
		cg2.setCookieSecure(true); // https로만 통신할 때만 쿠키를 전송한다는 옵션
		cg2.setCookieHttpOnly(true);// 보안을위해 http-only 사용
		cg2.setCookieMaxAge(time); // 60초 * 60분 * 24시간 * 7일
		cg2.addCookie(response, refreshToken);
	}

	//로그인시 db에 토큰 저장하는 것 
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

	public void newToken(Cookie refreshToken, HttpServletResponse response) {
		if(refreshToken==null) // 리프레쉬토큰을 못불러올경우 에러임
		{
			throw new RuntimeException("403");
		}
		TokenDataDTO tokenDataDTO = tokenDataRepository.findByRefreshToken(refreshToken.getValue())
												.map(token-> token.toDTO(token))
												.orElseThrow(() -> new RuntimeException("403"));
		// 토큰유효시간
		tokenProvider.validateToken(tokenDataDTO.getRefreshToken());
		
//		emp에서 auth를 꺼낸 후 토큰과 쿠키를 각각 넣는다.
		EmployeeDTO empDTO = empolyServiceImpl.findByEmpId(tokenDataDTO.getEmpId())
												.map(emp->emp.toDTO(emp))
												.orElseThrow(() -> new RuntimeException("저장된 유저 데이터가 없습니다."));
		long randumNumber = random.nextInt()%30000; // -30초부터 +30초까지 사이랜덤 값생성으로 리프레쉬토큰의 값을 변경함
		long timeDiff = tokenDataDTO.getExpireTime().getTime()-(new Date()).getTime()+randumNumber;// 밀리세컨드
		tokenDataDTO.setAccessToken(tokenProvider.createAccessToken(empDTO.getEmpId(), String.valueOf(empDTO.getAuthority())));
		tokenDataDTO.setRefreshToken(tokenProvider.createRefreshToken(empDTO.getEmpId(), String.valueOf(empDTO.getAuthority()), new Date((new Date().getTime())+timeDiff)));
		saveCookieAccessToken(response,tokenDataDTO.getAccessToken());
		saveCookieRefreshToken(response,tokenDataDTO.getRefreshToken(),Long.valueOf(timeDiff/1000).intValue());
		tokenDataRepository.save(tokenDataDTO.toEntity(tokenDataDTO));

		}

}
