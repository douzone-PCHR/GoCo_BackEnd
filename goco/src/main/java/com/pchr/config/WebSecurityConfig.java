package com.pchr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

                .and()
//마지막으로 전에 설정한 JwtSecurityConfig클래스를 통해 tokenProvider를 적용시킨다.
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}