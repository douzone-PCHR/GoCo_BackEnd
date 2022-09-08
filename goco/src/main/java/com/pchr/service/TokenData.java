package com.pchr.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public interface TokenData {
	 void insertCookies(HttpServletResponse response, String access , String refresh);
	 void saveCookieAccessToken(HttpServletResponse response, String accessToken);
	 void saveCookieRefreshToken(HttpServletResponse response, String refreshToken, int time);
	 void saveTokens(String accessToken, String refreshToken, String empId);
	 void newToken(Cookie refreshToken, HttpServletResponse response);
	 void deleteData();
}
