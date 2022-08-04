package com.pchr.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.entity.Employee;
import com.pchr.repository.EmployeeRepository;
import com.pchr.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
//실제 로직들이 수행되는 장소
public class MemberServiceImpl implements MemberService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
	@Override
//getMyInfoBySecurity는 헤더에 있는 token값을 토대로 Member의 data를 건내주는 메소드다
    public EmployeeResponseDTO getMyInfoBySecurity() {
        return employeeRepository.findByEmpId(SecurityUtil.getCurrentMemberId())
                .map(EmployeeResponseDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
	@Override
//changeMemberPassword는 패스워드 변경이다. 패스워드 변경 또한 token값을 토대로 찾아낸 member를 찾아낸 다음 제시된 예전 패스워드와 DB를 비교한다.
    @Transactional
    public EmployeeResponseDTO changeMemberPassword(String exPassword, String newPassword) {
        Employee employee = employeeRepository.findByEmpId(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, employee.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        employee.setPassword(passwordEncoder.encode((newPassword)));
        return EmployeeResponseDTO.of(employeeRepository.save(employee));
    }
	
}
