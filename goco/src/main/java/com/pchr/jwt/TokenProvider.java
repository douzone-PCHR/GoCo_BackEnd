package com.pchr.jwt;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import com.pchr.config.SecurityUtil;
import com.pchr.dto.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class TokenProvider {
	
    private static final String AUTHORITIES_KEY = "auth";//토큰 생성, 검증할 때 쓰이는 값
    private static final String AUTHORITIES_KEY2 = "refreshTokenAuth";//토큰 생성, 검증할 때 쓰이는 값
    private static final String BEARER_TYPE = "bearer"; //토큰 생성, 검증할 때 쓰이는 값
    private static final long ACCESS_TOKEN_EXPIRE_TIME =30 * 60 * 1000L;// 토큰 만료시간 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME =60 * 60 *24 * 7 * 1000L;// 토큰 만료시간 일주일뒤
    
    private final Key key; //JWT를 만들 때 사용하는 암호화 키값을 사용학 ㅣ위해 security에서 불러옴

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    // 엑세스토큰 만드는것 
    public TokenDTO generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date tokenExpiresIn = new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = createAccessToken(authentication.getName(),authorities);
        String refreshToken = createRefreshToken(authentication.getName(),authorities,new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRE_TIME));
         
        
        return TokenDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .build();
    } 
    
    // 엑세스 토큰 생성
    public String createAccessToken(String empId,String Auth) {
    	return Jwts.builder()
                .setSubject(empId)
                .claim(AUTHORITIES_KEY, Auth)
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRE_TIME))//토큰 유효시간설정
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public String createRefreshToken(String empId,String Auth,Date time) {
    	return Jwts.builder()
			    .setSubject(empId)
			    .claim(AUTHORITIES_KEY2, Auth) // 키값넣는것 
			    .setExpiration(time)//리프레쉬 토큰 유효시간 7일
			    .signWith(key, SignatureAlgorithm.HS512)
			    .compact();
    	
    }

    // JWT 토큰에서 인증 정보 조회 , SecurityContextHolder에 저장하기 전 데이터를 가공하는 것이다. 
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //토큰을 검증하기 위한 메소드다.
    public boolean validateToken(HttpServletResponse response,String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        	System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) { // 토큰 만료시 login페이지 이동 
    		response.addHeader("refresh", "false");
 			throw new RuntimeException("redirectedToLogin");  
        } catch (UnsupportedJwtException e) {
        	System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
        	System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    //토큰을 claims형태로 만드는 메소드다. 이를 통해 위에서 권한 정보가 있는지 없는지 체크가 가능하다.
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    // 쿠키 리셋 
    public void cookieReset(HttpServletResponse response) {
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("refreshToken");
		cg.setCookieMaxAge(0);
		cg.addCookie(response, "1");
		cg.setCookieName("accessToken");
		cg.setCookieMaxAge(0);
		cg.addCookie(response, "1");
		SecurityUtil.contextReset();
    }
	public String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
	}
	public String getRefreshToken(HttpServletRequest request) {
		if(request.getCookies()!=null) {
		    	 for (Cookie eachCookie : request.getCookies()) {
		    		 if(eachCookie.getName().equals("refreshToken")) {
		    			 return eachCookie.getValue();
		    		 }
		         }
	   	}
	   	return null;
	}
    
}