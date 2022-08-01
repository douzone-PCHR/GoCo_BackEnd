package com.pchr.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.MemberRequestDTO;
import com.pchr.dto.MemberResponseDTO;
import com.pchr.dto.TokenDTO;
import com.pchr.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDTO> signup(@RequestBody MemberRequestDTO requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }
//    {
//    	"email" : "token-test@test.com",
//    	"password" : "test1234",
//    	"nickname" : "token-tester"
//    }
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody MemberRequestDTO requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
//    {
//    	"email" : "token-test@test.com",
//    	"password" : "test1234"
//    } 
    
}