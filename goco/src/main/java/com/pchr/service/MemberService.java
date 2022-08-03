package com.pchr.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.entity.Employee;
import com.pchr.repository.EmployeeRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
//실제 로직들이 수행되는 장소
public class MemberService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

//getMyInfoBySecurity는 헤더에 있는 token값을 토대로 Member의 data를 건내주는 메소드다
    public EmployeeResponseDTO getMyInfoBySecurity() {
        return employeeRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(EmployeeResponseDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

//changeMemberNickname는 닉네임 변경이다.   
//    @Transactional
//    public MemberResponseDTO changeMemberNickname(String email, String nickname) {
//        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
//        member.setNickname(nickname);
//        return MemberResponseDTO.of(memberRepository.save(member));
//    }

//changeMemberPassword는 패스워드 변경이다. 패스워드 변경 또한 token값을 토대로 찾아낸 member를 찾아낸 다음 제시된 예전 패스워드와 DB를 비교한다.
    @Transactional
    public EmployeeResponseDTO changeMemberPassword(String exPassword, String newPassword) {
        Employee employee = employeeRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, employee.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        employee.setPassword(passwordEncoder.encode((newPassword)));
        return EmployeeResponseDTO.of(employeeRepository.save(employee));
}
}