package com.pchr.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() { }

//SecurityContext에 유저 정보가 저장되는 시점을 다루는 클래스다.
//Request가 들어오면 JwtFilter의 doFilter에서 저장되는데 거기에 있는 인증정보를 꺼내서, 그 안의 id를 반환한다.
//우리는 Entity를 정할때 id의 타입을 Long으로 했기 때문에 Long을 반환한다.
    public static String getCurrentMemberId() {
    	
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        
        return authentication.getName();

    }
}