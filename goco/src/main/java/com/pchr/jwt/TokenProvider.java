package com.pchr.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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
    private static final String BEARER_TYPE = "bearer"; //토큰 생성, 검증할 때 쓰이는 값
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;// 토큰 만료시간
    private final Key key; //JWT를 만들 때 사용하는 암호화 키값을 사용학 ㅣ위해 security에서 불러옴



    // 주의점: 여기서 @Value는 `springframework.beans.factory.annotation.Value`소속이다! lombok의 @Value와 착각하지 말것!
    //     * @param secretKey
    // Value는 application.properties에 있는 secret Key를 가져온 다음 이걸 decode함, 이후 의존성이 주입된 key의 값으로 정함
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // generateTokenDto이것은 토큰을 만드는 메소드이다 Authentication 인터페이스를 확장한
    // 매개 변수를 받아서 그 값을 String으로 으로 변환 ,  이후 현재 시간, 만료시간을 만든 후 Jwts의 builder를 이용해 Token을 생성하고
    // TokenDto에 생성한 token의 정보를 넣는다.
    public TokenDTO generateTokenDto(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();


        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        System.out.println(tokenExpiresIn);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .build();
    }

    //getAuthentication메소드는 토큰을 받았을 때 토큰의 인증을 꺼내는 메소드이다. 아래 서술할 parseClaims 메소드로
    //String 형태의 토큰을 claims 형태로 생성한다. 다음 auth가 없으면 exception을 반환한다.
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
// GrantedAuthority를 상속받은 타입만이 사용 가능한 Collection을 반환한다.
// 그리고 stream을 통한 함수형 프로그래밍으로 claims형태의 토큰을 알맞게 정렬한 이후 SimpleGrantedAuthority
//형태의 새 List를 생성한다. 여기에 인가가 들어 있다. 
// SimpleGrantedAuthority은 GrantedAuthority을 상속받았기 때문에 이 지점이 가능하다.
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
//이후 Spring Security에서 유저의 정보를 담는 인터페이스인 UserDetails에 token에서 발췌한 정보와, 아까 생성한 인가를 넣고,
        UserDetails principal = new User(claims.getSubject(), "", authorities);
//이를 다시 UsernamePasswordAuthenticationToken안에 인가와 같이 넣고 반환한다.
//여기서 UsernamePasswordAuthenticationToken인스턴스는 UserDetails를 
//생성해서 후에 SecurityContext에 사용하기 위해 만든 절차라고 이해하면 된다.
//왜냐하면 SecurityContext는 Authentication객체를 저장하기 때문이다.
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

//토큰을 검증하기 위한 메소드다.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        	System.out.println("잘못된 JWT 서명입니다.");
         //   logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
        	System.out.println("만료된 JWT 토큰입니다.");
//        	LOG.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
        	System.out.println("지원되지 않는 JWT 토큰입니다.");
//        	LOG.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
        	System.out.println("JWT 토큰이 잘못되었습니다.");
//        	LOG.info("JWT 토큰이 잘못되었습니다.");
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
}