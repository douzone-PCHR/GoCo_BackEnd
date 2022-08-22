package com.pchr.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.pchr.jwt.JwtAccessDeniedHandler;
import com.pchr.jwt.JwtAuthenticationEntryPoint;
import com.pchr.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    //request로부터 받은 비밀번호를 암호화하기 위해 PasswordEncoder 빈을 생성했다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//WebSecurityConfigurerAdapter는 22년 해당 기능을 deprecated했다.
//대신 HttpSecurity를 Configuring해서 사용하라는 대안방식을 제시
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        		.formLogin();// 인가/인증 절차에서문제 발생시 로그인 페이지를 보여주도록 함
        http
//우리는 리액트에서 token을 localstorage에 저장할 것이기 때문에 csrf 방지또한 disable했다.
                .csrf().disable()
//또한 우리는 REST API를 통해 세션 없이 토큰을 주고받으며 데이터를 주고받기 때문에 세션설정또한 STATELESS로 설정했다.                
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
//이후 예외를 핸들링하는 것에서는 이전에 작성했던 JwtAuthenticationEntryPoint와 JwtAccessDeniedHandler를 넣었다.
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .authorizeRequests()
//모든 Requests에 있어서 /auth/**를 제외한 모든 uri의 request는 토큰이 필요하다. /auth/**는 로그인 페이지를 뜻한다.
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/**").permitAll()
                //.antMatchers("/api/user/**").access("hasRole('ADMIN') or hasRole('USER')or hasRole('MANAGER')")
                //.antMatchers("/api/MANAGER/**").access("hasRole('ADMIN') or hasRole('MANAGER')")
                //.antMatchers("/api/admin/**").access("hasRole('ADMIN')")
                .anyRequest().authenticated()
                .and()
//마지막으로 전에 설정한 JwtSecurityConfig클래스를 통해 tokenProvider를 적용시킨다.
                .apply(new JwtSecurityConfig(tokenProvider))
                //이하 로그아웃 
                .and()
                .logout() // 로그아웃 처리
                .logoutUrl("/auth/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/auth/login") // 로그아웃 성공 후 이동페이지
                
                .addLogoutHandler(new LogoutHandler() {
					@Override
					public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		                HttpSession session = request.getSession();
		                session.invalidate();
					}
				})
                .logoutSuccessHandler(new LogoutSuccessHandler() { //logout 시 이동할 url 및 더 많은 로직 구현 가능
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/auth/login"); //로그아웃 시 로그인 할 수 있는 페이지로 이동하도록 처리한다.
                    }
                })
                .deleteCookies("JSESSIONID", "remember-me","accessToken");// 로그아웃 후 해당 쿠키 삭제
        return http.build();
    }
}