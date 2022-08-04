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

	private Boolean deleteYn;

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

	// -------------- to Entity ------------------- //
	public Employee toEntity(EmployeeDTO employeeDTO) {
		Employee employee = Employee.builder()
							.empNum(employeeDTO.getEmpNum())
							.empId(employeeDTO.getEmpId())
							.password(employeeDTO.getPassword())
							.email(employeeDTO.getEmail())
							.name(employeeDTO.getName())
							.phoneNumber(employeeDTO.getPhoneNumber())
							.deleteYn(employeeDTO.getDeleteYn())
							.updateDatetime(employeeDTO.getUpdateDatetime())
							.hiredate(employeeDTO.getHiredate())
							.authority(employeeDTO.getAuthority())
							.vacationCount(employeeDTO.getVacationCount())
							.manager(toManager(employeeDTO.getManager()))
							.jobTitle(employeeDTO.getJobTitle().toEntity(employeeDTO.getJobTitle()))
							.teamPosition(employeeDTO.getTeamPosition().toEntity(employeeDTO.getTeamPosition()))
							//.unit 넣어야함
							.build();
		return employee;
	}
	public Employee toManager(EmployeeDTO employeeDTO) {
		Employee employee = Employee.builder()
										.empNum(employeeDTO.getEmpNum())
										.empId(employeeDTO.getEmpId())
										.password(employeeDTO.getPassword())
										.email(employeeDTO.getEmail())
										.name(employeeDTO.getName())
										.phoneNumber(employeeDTO.getPhoneNumber())
										.deleteYn(employeeDTO.getDeleteYn())
										.updateDatetime(employeeDTO.getUpdateDatetime())
										.hiredate(employeeDTO.getHiredate())
										.authority(employeeDTO.getAuthority())
										.vacationCount(employeeDTO.getVacationCount())
										//.manager(toManager(employeeDTO.getManager()))
										.jobTitle(employeeDTO.getJobTitle().toEntity(employeeDTO.getJobTitle()))
										.teamPosition(employeeDTO.getTeamPosition().toEntity(employeeDTO.getTeamPosition()))
										//.unit 넣어야함
										.build();	
		return employee;

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
