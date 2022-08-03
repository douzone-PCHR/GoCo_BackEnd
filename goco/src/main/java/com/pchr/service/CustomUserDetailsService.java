package com.pchr.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pchr.entity.Employee;

import com.pchr.repository.EmployeeRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//실제 로직들이 수행되는 장소
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Override
//실행한 loadUserByUsername은 받은 empId를 통해 user가 실제로 존재하는지 알아보는 메소드다. 존재하지 않으면 예외를 날린다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findByEmpId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " 을 DB에서 찾을 수 없습니다"));
    }

    private UserDetails createUserDetails(Employee employee) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(employee.getAuthority().toString());

        return new User(
                String.valueOf(employee.getEmpId()),//★★★getEmpId 가 아닐 수도 있다.
                employee.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}