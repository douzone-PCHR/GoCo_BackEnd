package com.pchr.dto;

import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pchr.entity.Authority;
import com.pchr.entity.Employee;
import com.pchr.entity.JobTitle;
import com.pchr.entity.TeamPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Request를 받을 때 쓰이는 dto다.
public class EmployeeRequestDTO {
	private String empId;
	private String password;
	private String name;
	private String phoneNumber;
	private String email;
	private Date hiredate;
	private JobTitle jobTitle;
	private TeamPosition teamPosition;
    
    public Employee toEmployee(PasswordEncoder passwordEncoder) {
    	return Employee.builder()
    			.empId(empId)
    			.password(passwordEncoder.encode(password))
    			.name(name)
    			.phoneNumber(phoneNumber)
    			.email(email)
    			.hiredate(hiredate)
    			.jobTitle(jobTitle)
    			.teamPosition(teamPosition)
    			.authority(Authority.ROLE_USER)
    			.build();
    }
 // UsernamePasswordAuthenticationToken를 반환하여 아이디와 비밀번호가 일치하는지 검증하는 로직을 넣을 수 있게 된다.
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(empId, password);
    }    
    
}
