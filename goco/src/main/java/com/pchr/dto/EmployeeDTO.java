package com.pchr.dto;

import java.util.Date;

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

	private int authority;

	private int vacationCount;

	// 팀장에 대한 필드
	private EmployeeDTO manager;

	// 직위에 대한 필드
	private JobTitleDTO jobTitle;

	// 팀에 속한 위치에 대한 필드
	private TeamPositionDTO teamPosition;

	// 어느 팀에 소속된지에 대한 필드
	private UnitDTO unit;

	// -------------- to Entity ------------------- //
	public Employee toEntity(EmployeeDTO employeeDTO) {
		Employee employee = Employee.builder().empNum(employeeDTO.getEmpNum()).build();
		return employee;
	}
}
