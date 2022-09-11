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

import com.pchr.service.impl.TokenDataImpl;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    


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

    // 필터링 실행 메소드 , 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	Cookie refreshToken = getRefreshToken(request);
        String accessToken = getAccessToken(request);
        if(accessToken.equals("undefined")&&refreshToken!=null) {//엑세스 만료 + 리프레쉬있음
        	if(tokenProvider.validateToken(refreshToken.getValue())) {//리프레쉬 만료일 체크, 
        		
        	}

        }
//validateToken으로 토큰이 유효한지 검사를 해서,만약 유효하다면 Authentication을 가져와서 SecurityContext에 저장한다
//SecurityContext에서 허가된 uri 이외의 모든 Request 요청은 전부 이 필터를 거치게 되며, 토큰 정보가 없거나 유효치않으면 정상적으로 수행되지 않는다.
//반대로 Request가 정상적으로 Controller까지 도착했으면 SecurityContext에 Member ID가 존재한다는 것이 보장이 된다.
        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        System.out.println("=======================절취선========================");
        filterChain.doFilter(request, response);
    }
}