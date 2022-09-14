package com.pchr.jwt;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.pchr.repository.TokenDataRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final  TokenDataRepository tokenDataRepository;
    private String[] exceptURL = {"/api/user/menu/commute","/api/auth/login","/api/auth/logOut","/api/auth/getAllUnit","/api/auth/checkInfo",
    		"/api/auth/sendEmailForEmail","/api/auth/find/1","/api/auth/signup","/api/auth/sendEmailForId","/api/auth/find/2",
    		"/api/auth/sendEmailForPwd","/api/auth/find/3"};

    // 리프레쉬토큰으로 엑세스 생성 
    private void createCookie(HttpServletRequest request, HttpServletResponse response,String refreshToken) {
         if(tokenProvider.validateToken(response,refreshToken)) {//리프레쉬 만료일 체크, 
         	if(!request.getRequestURI().equals("/api/user/newtoken")) {
         		if(!request.getRequestURI().equals("/api/auth/logOut")) {
         			response.addHeader("refresh", "true");
         			throw new RuntimeException("getAccessToken");        				
         		}
         	}
         }
    }
    // 엑세스 + 리프레쉬 없을 때 로그인으로 보내느 것
    private void redirectedToLogin(HttpServletRequest request, HttpServletResponse response) {
        int check =0;
    	for (String string : exceptURL) {
			if(!request.getRequestURI().equals(string)) {
				++check;
			}
		}
    	if(check!=11) {
			System.out.println("URL : "+request.getRequestURI());
				tokenProvider.cookieReset(response);
				response.addHeader("refresh", "false");
				throw new RuntimeException("redirectedToLogin");  
    	}
    }
    // 쿠키가 없을 때 로그인 페이지로 이동해줌 
    private void CookieCheck(String accessToken,String refreshToken,HttpServletRequest request, HttpServletResponse response) {
    	if(accessToken==null&&refreshToken==null||refreshToken==null&&accessToken.length()<110) {
    		redirectedToLogin(request,response);
    	}
    }
    // 필터링 실행 메소드 , 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	String refreshToken = tokenProvider.getRefreshToken(request);
        String accessToken = tokenProvider.getAccessToken(request);
        CookieCheck(accessToken,refreshToken,request,response);
        if(refreshToken!=null) {
	        if(accessToken.equals("undefined")&&tokenDataRepository.existsByRefreshToken(refreshToken)) {
	        	createCookie(request,response, refreshToken);// 리프레쉬가 있고, 디비에도 있으며, 엑세스가 없어서 리프레쉬로 엑세스를 받아야될 때
	        }
	        else if(!tokenDataRepository.existsByRefreshToken(refreshToken)) {
	        	//리프레쉬가 있지만 디비에 없는 경우 쿠키 삭제 및 로그인 페이지 이동 
	        	redirectedToLogin(request,response);
	        }
        }
		//validateToken으로 토큰이 유효한지 검사를 해서,만약 유효하다면 Authentication을 가져와서 SecurityContext에 저장한다
		//SecurityContext에서 허가된 uri 이외의 모든 Request 요청은 전부 이 필터를 거치게 되며, 토큰 정보가 없거나 유효치않으면 정상적으로 수행되지 않는다.
		//반대로 Request가 정상적으로 Controller까지 도착했으면 SecurityContext에 Member ID가 존재한다는 것이 보장이 된다.
        if (tokenProvider.validateToken(response,accessToken)&&tokenDataRepository.existsByAccessToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else if(!tokenDataRepository.existsByAccessToken(accessToken)&&accessToken.length()>110) {//엑세스 토큰이 전달되었으나 db에 없는 경우 로그인 페이지 이동   
        	redirectedToLogin(request,response);
        }
        filterChain.doFilter(request, response);
    }
}