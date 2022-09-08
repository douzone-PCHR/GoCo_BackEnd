package com.pchr.api;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pchr.service.impl.TokenDataImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class TokenController {
	private final TokenDataImpl tokenDataImpl;
	@GetMapping("/user/newtoken")
	public void newtoken(@CookieValue(value="refreshToken", required = false) Cookie refreshToken,HttpServletResponse response) {
		tokenDataImpl.newToken(refreshToken,response);		  
	}
}
