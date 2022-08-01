package com.pchr.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.MemberResponseDTO;
import com.pchr.entity.Member;
import com.pchr.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//getMyInfoBySecurity는 헤더에 있는 token값을 토대로 Member의 data를 건내주는 메소드다
    public MemberResponseDTO getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

//changeMemberNickname는 닉네임 변경이다.   
    @Transactional
    public MemberResponseDTO changeMemberNickname(String email, String nickname) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        member.setNickname(nickname);
        return MemberResponseDTO.of(memberRepository.save(member));
    }

//changeMemberPassword는 패스워드 변경이다. 패스워드 변경 또한 token값을 토대로 찾아낸 member를 찾아낸 다음 제시된 예전 패스워드와 DB를 비교한다.
    @Transactional
    public MemberResponseDTO changeMemberPassword(String exPassword, String newPassword) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        member.setPassword(passwordEncoder.encode((newPassword)));
        return MemberResponseDTO.of(memberRepository.save(member));
}
}