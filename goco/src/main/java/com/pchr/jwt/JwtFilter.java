package com.pchr.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.pchr.repository.TokenDataRepository;
import com.pchr.service.impl.TokenDataImpl;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final  TokenDataRepository tokenDataRepository;


    // 엑세스 토큰 받아오기 
    private String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
    // 리프레쉬 토큰 받아오기 
    private Cookie getRefreshToken(HttpServletRequest request) {
    	if(request.getCookies()!=null) {
	    	 for (Cookie eachCookie : request.getCookies()) {
	    		 if(eachCookie.getName().equals("refreshToken")) {
	    			 return eachCookie;
	    		 }
	         }
    	}
    	return null;
    }
    // 리프레쉬토큰으로 엑세스 생성 
    private void createCookie(HttpServletRequest request, HttpServletResponse response,Cookie refreshToken) {
         if(tokenProvider.validateToken(refreshToken.getValue())) {//리프레쉬 만료일 체크, 
         	if(!request.getRequestURI().equals("/api/user/newtoken")) {
         		if(!request.getRequestURI().equals("/api/auth/logOut")) {
         			response.addHeader("refresh", "true");
         			throw new RuntimeException("getAccessToken");        				
         		}
         	}
         }
    }
    // 엑세스 + 리프레쉬 없을 때 로그인으로 보내느 것
    
    
    // 현재 상태를 반환해주는 메소드 
    private int checkCookie(String accessToken, Cookie refreshToken) {
    	if(accessToken.equals("undefined")&&refreshToken!=null&&tokenDataRepository.existsByRefreshToken(refreshToken.getValue())) {
    		return 1; // 리프레쉬가 있고, 디비에도 있으며, 엑세스가 없어서 리프레쉬로 엑세스를 받아야될 때
    	}else if (accessToken.equals("undefined")&&refreshToken==null) {
    		return 2; //리프레쉬, 엑세스 둘다 없을 때 
    	}
    		return 0;
    }

    // 필터링 실행 메소드 , 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	Cookie refreshToken = getRefreshToken(request);
        String accessToken = getAccessToken(request);
        switch(checkCookie(accessToken,refreshToken)) {
        case 1:
        	createCookie(request,response, refreshToken);break;
        case 2:  // 리프레쉬, 엑세스 둘다없을 때
         	if(!request.getRequestURI().equals("/api/user/menu/commute")) {
         		if(!request.getRequestURI().equals("/api/auth/login")) {
         			response.addHeader("refresh", "false");
         			throw new RuntimeException("getTwoToken");        				
         		}
         	}
        	break;
        default : 
        	break;
        }
       
        // 엑세스, 리프레쉬 둘다없으면 login으로넘겨야되는데, 그때 사용되는 url이 : /user/menu/commute   + /auth/login
        
        
//validateToken으로 토큰이 유효한지 검사를 해서,만약 유효하다면 Authentication을 가져와서 SecurityContext에 저장한다
//SecurityContext에서 허가된 uri 이외의 모든 Request 요청은 전부 이 필터를 거치게 되며, 토큰 정보가 없거나 유효치않으면 정상적으로 수행되지 않는다.
//반대로 Request가 정상적으로 Controller까지 도착했으면 SecurityContext에 Member ID가 존재한다는 것이 보장이 된다.
        if (tokenProvider.validateToken(accessToken)&&tokenDataRepository.existsByAccessToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        System.out.println("=======================절취선========================");
        filterChain.doFilter(request, response);
    }
}