package com.pchr.dto;

import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

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
		if(employeeDTO.getManager() != null) {// 매니저가 있으면 아래 실행
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
		return toManager(employeeDTO); // 매니저가 없으면 실행 
	}
	
// /*Insert || Update 과정에서는 매니저에 대한 ID값만 넘겨주면 됨*/
//	public Employee toManager(EmployeeDTO employeeDTO) {
//		Employee employee = Employee.builder()
//										.empNum(employeeDTO.getEmpNum())
//										.empId(employeeDTO.getEmpId())
//										.password(employeeDTO.getPassword())
//										.email(employeeDTO.getEmail())
//										.name(employeeDTO.getName())
//										.phoneNumber(employeeDTO.getPhoneNumber())
//										.deleteYn(employeeDTO.getDeleteYn())
//										.updateDatetime(employeeDTO.getUpdateDatetime())
//										.hiredate(employeeDTO.getHiredate())
//										.authority(employeeDTO.getAuthority())
//										.vacationCount(employeeDTO.getVacationCount())
//										//.manager(toManager(employeeDTO.getManager()))
//										.jobTitle(employeeDTO.getJobTitle().toEntity(employeeDTO.getJobTitle()))
//										.teamPosition(employeeDTO.getTeamPosition().toEntity(employeeDTO.getTeamPosition()))
//										//.unit 넣어야함
//										.build();	
//		return employee;
//	}
	public Employee toFKEmployee(EmployeeDTO empDto) {
		Employee fkEmp = Employee.builder().empNum(empDto.getEmpNum()).build();
		return fkEmp;
	}

	public Employee toEmployee(EmployeeDTO empDto) {
		Employee employee = Employee.builder().authority(empDto.getAuthority())
				.manager(empDto.getManager().toManager(empDto.getManager()))
				.jobTitle(empDto.getJobTitle().toEntity(empDto.getJobTitle()))
				.build();
		return null;
	}

	public Employee toManager(EmployeeDTO employeeDTO) {
		Employee employee = Employee.builder()
				.authority(employeeDTO.getAuthority())
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
	}
	
	//회원 가입할 때 사용 
    public Employee toEmpSignUp(PasswordEncoder passwordEncoder) {
    	return Employee.builder()
    			.empId(empId)
    			.password(passwordEncoder.encode(password))
    			.name(name)
    			.phoneNumber(phoneNumber)
    			.email(email)
    			.hiredate(hiredate)
    			.jobTitle(jobTitle.toEntity(jobTitle))
    			.teamPosition(teamPosition.toEntity(teamPosition))
    			.authority(Authority.ROLE_USER)
    			.build();
    }
    // UsernamePasswordAuthenticationToken를 반환하여 아이디와 비밀번호가 일치하는지 검증하는 로직을 넣을 수 있게 된다.
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(empId, password);
    }    
    //EmployeeResponseDTO 에있던 of함수 만든것
//    public static EmployeeDTO of(Employee employee) {
//    	return EmployeeDTO.builder()
//    			.email(employee.getEmail())
//    			.empId(employee.getEmpId())
//    			.unit(employee.getUnit().toFKUnitDto(employee.getUnit()))
//    			.build();
//    }
}
