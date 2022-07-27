//package com.pchr.security;
//
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//
//
//@Configuration // 설정클래스인것을 선언하는 것이다. 
//@EnableWebSecurity // 이 어노테이션은 WebSecurityConfiguration 클래스 등을 Import해서 실행시키는 역할을 하는 어노테이션, 
//// @EnableWebSecurity 이것을 해야 웹보안이 활성화된다. 
//public class SecurityConfig  {
//	@Autowired
//	UserDetailsService userDetailsService;
//	
//    @Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws
//		Exception {
//
//		http // 인가 정책
//			.authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정하겠다는 의미 
//			.anyRequest().authenticated(); // 다른 요청들(anyRequest)에 대해서는 인증을 받아야된다는뜻
//		// permitAll은 요청에 대해 인증 없이 접근을 허용하겠다는 의미이다.  
//		
//		http // 인증 정책
//			.formLogin();
//		http
//			.logout()
//			.logoutUrl("/logout")
//			.logoutSuccessUrl("/login")//로그아웃 성공 후 이동할 페이지
//			.addLogoutHandler(new LogoutHandler() {
//				
//				@Override
//				public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//					HttpSession session = request.getSession();
//					session.invalidate();	
//				}
//			})
//			//logoutSuccessUrl과 비슷하지만, logoutSuccessHandler가 더 다양한 코딩이 가능하다.
//			.logoutSuccessHandler(new LogoutSuccessHandler() {
//				
//				@Override
//				public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//						throws IOException, ServletException {
//						response.sendRedirect("http://www.naver.com");
//				}
//			})
//			.deleteCookies("remember-me")
//		.and()
//			.rememberMe() //리맴버미 설정
//			.rememberMeParameter("remember-KYJ")
//			.tokenValiditySeconds(3600)//초단위 default 14일
//			.alwaysRemember(true)//리맴버미기능이 활성화되지 않아도 항상 실행
//			.userDetailsService(userDetailsService)//리맴버미 기능수행시 시스템 사용자 계정을 조회할때 필요한 클래스 필수로해주어야함
//			//userDetailsService는 @Autowired를 해주어야 한다. 
//			;
//		http
//			.sessionManagement()
//			.sessionFixation().newSession()//세션을 항상 새로만듦
//			.maximumSessions(1)// 최대 허용가능 세션 갯수
//			.maxSessionsPreventsLogin(true)//값을 true로 줄 경우 동시 로그인이 차단된다.
//			
//			
//;
//		return http.build();
//	}
//}
