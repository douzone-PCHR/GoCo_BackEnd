package com.pchr.dto;

import java.util.Date;

import com.pchr.entity.Authority;
import com.pchr.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

	private Long empNum;

	private String empId;

	private String password;

	private String email;

	private String name;

	private String phoneNumber;

	private boolean deleteYn;

	private Date updateDatetime;

	private Date hiredate;

	private Authority authority;

	private int vacationCount;

	// 팀장에 대한 필드
	private EmployeeDTO manager;

	// 직위에 대한 필드
	private JobTitleDTO jobTitle;

	// 팀에 속한 위치에 대한 필드
	private TeamPositionDTO teamPosition;

	// 어느 팀에 소속된지에 대한 필드
	private UnitDTO unit;

	public Employee toFKEmployee(EmployeeDTO empDto) {
		Employee fkEmp = Employee.builder().empNum(empDto.getEmpNum()).build();
		return fkEmp;
	}
	public Employee toEmployee(EmployeeDTO empDto) {
		Employee employee = Employee.builder().authority(empDto.getAuthority())
				.mgrId(empDto.getManager().toManager(empDto.getManager()))
				.jobTitle(empDto.getJobTitle().toJobTitle(empDto.getJobTitle()))
				.build();
		return null;
	}
	
	public Employee toManager(EmployeeDTO empDto) {
		
		Employee employee = Employee.builder().authority(empDto.getAuthority())
				
				.build();
		return null;
	}
}
