//package com.pchr.api;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.pchr.dto.ChangePasswordRequestDTO;
//import com.pchr.dto.MemberRequestDTO;
//import com.pchr.dto.MemberResponseDTO;
//import com.pchr.service.MemberService;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/member")
//public class MemberController {
//
//    private final MemberService memberService;
//
//    @GetMapping("/me")
//    public ResponseEntity<MemberResponseDTO> getMyMemberInfo() {
//        MemberResponseDTO myInfoBySecurity = memberService.getMyInfoBySecurity();
//        System.out.println(myInfoBySecurity.getNickname());
//        return ResponseEntity.ok((myInfoBySecurity));
//        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
//    }
//
//    @PostMapping("/nickname")
//    public ResponseEntity<MemberResponseDTO> setMemberNickname(@RequestBody MemberRequestDTO request) {
//        return ResponseEntity.ok(memberService.changeMemberNickname(request.getEmail(), request.getNickname()));
//    }
//
//    @PostMapping("/password")
//    public ResponseEntity<MemberResponseDTO> setMemberPassword(@RequestBody ChangePasswordRequestDTO request) {
//        return ResponseEntity.ok(memberService.changeMemberPassword(request.getExPassword(), request.getNewPassword()));
//    }
//
//}