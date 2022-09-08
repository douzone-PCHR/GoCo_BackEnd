package com.pchr.service.impl;

import java.util.Date;

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
		
//		emp에서 auth를 꺼내온다. 그리고 create토큰으로 2개를 만들어 준다. 
		EmployeeDTO empDTO = empolyServiceImpl.findByEmpId(tokenDataDTO.getEmpId())
												.map(emp->emp.toDTO(emp))
												.orElseThrow(() -> new RuntimeException("저장된 유저 데이터가 없습니다."));
		long timeDiff = tokenDataDTO.getExpireTime().getTime()-(new Date()).getTime();
		System.out.println("time diff : "+timeDiff);
		tokenDataDTO.setAccessToken(tokenProvider.createAccessToken(empDTO.getEmpId(), String.valueOf(empDTO.getAuthority())));
		tokenDataDTO.setRefreshToken(tokenProvider.createRefreshToken(empDTO.getEmpId(), String.valueOf(empDTO.getAuthority()), new Date((new Date().getTime())+timeDiff)));
		saveCookieAccessToken(response,tokenDataDTO.getAccessToken());
	//	saveCookieRefreshToken(response,tokenDataDTO.getRefreshToken(),);
//		tokenProvider.createRefreshToken(empDTO.getEmpId(),
//					String.valueOf(empDTO.getAuthority()),
//					new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRE_TIME));
		// 리프레시 시간 찾아야됨
//		tokenDataDTO.setRefreshToken(tokenProvider.createRefreshToken(empDTO.getEmpId(), String.valueOf(empDTO.getAuthority()), tokenDataDTO.getExpireTime()));
		//기존 리프레시 토큰의 만료날짜를 구한다 , 현재날자를 구한다. 둘을 뺀다. 뺀값을 현제에서 더한다.
		
		//System.out.println("이거임 : "+(new Date()).getTime() + ACCESS_TOKEN_EXPIRE_TIME);//16626189736891800000
		
		
		
		

		System.out.println("현재 시간과 차이 : "+(tokenDataDTO.getExpireTime().getTime()-(new Date()).getTime()));
		System.out.println("저장된 시간 : "+tokenDataDTO.getExpireTime());
		System.out.println("계산된 시간 : "+new Date((new Date().getTime())+timeDiff));
		
		
		tokenDataRepository.save(tokenDataDTO.toEntity(tokenDataDTO));
		}
//	System.out.println("refreshToken.getName() : "+refreshToken.getName()); // 리프레쉬토큰 이라고 뜸 
//	System.out.println("refreshToken.getMaxAge() : "+refreshToken.getMaxAge()); // -1이라고 뜸 
//	System.out.println("refreshToken.getValue() : "+refreshToken.getValue()); // 쿠키의 길다란코드가 뜸  eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJreWo
//	System.out.println("refreshToken.getSecure() : "+refreshToken.getSecure()); // false 뜸 보안인듯 ? 
//	System.out.println("refreshToken.getVersion() : "+refreshToken.getVersion()); // 0 이라고 뜸 
//	System.out.println("refreshToken.getClass() : "+refreshToken.getClass()); // class javax.servlet.http.Cookie 뜸 
}
