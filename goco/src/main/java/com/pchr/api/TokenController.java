package com.pchr.api;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
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
	
	/**
	 * 토큰 재발급
	 * 
	 * @return ResponseEntity<?>
	 */
	@GetMapping("/user/newtoken")
	public ResponseEntity<?> newtoken(@CookieValue(value="refreshToken", required = false) Cookie refreshToken,HttpServletResponse response) {
		ResponseEntity<?> result = null;
		try {
			result = tokenDataImpl.newToken(refreshToken,response);		  
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
