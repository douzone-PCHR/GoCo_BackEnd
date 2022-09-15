package com.pchr.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.TokenDTO;

public interface TokenData {
	 void insertCookies(HttpServletResponse response, String access , String refresh);
	 void saveCookieAccessToken(HttpServletResponse response, String accessToken);
	 void saveCookieRefreshToken(HttpServletResponse response, String refreshToken, int time);
	 void saveTokens(String accessToken, String refreshToken, String empId);
	 ResponseEntity<?> newToken(Cookie refreshToken, HttpServletResponse response);
	 void deleteData();
	 boolean existsByRefreshToken(String refreshToken);
	 boolean existsByAccessToken(String accessToken);
	 int deleteByAccessToken(String accessToken);
	 int deleteByRefreshToken(String refreshToken);
	 public void cookiesSave(HttpServletResponse response, TokenDTO tokenDTO, EmployeeDTO employeeDTO);
}
