package com.pchr.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]{3,10}", message = "아이디는 영어 또는 숫자를 사용해 3 ~ 10자 내외로 입력해주세요.")
	private String empId;

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String password;

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Pattern(regexp = "(?=\\S+$).{2,30}", message = "이름은 공백을 제외하고 2글자 이상 입력해야 합니다.")
	private String name;

	@NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^(01[1|6|7|8|9|0])(\\d{3,4})(\\d{4})$", message = "핸드폰 번호 형식이 올바르지 않습니다.(10~11자리 입력)")
	private String phoneNumber;

	private Date updateDatetime;

	private Date hiredate;

	private Authority authority;

	private Float vacationCount;

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
		if (employeeDTO.getManager() != null) {// 매니저가 있으면 아래 실행
			Employee employee = Employee.builder().empNum(employeeDTO.getEmpNum()).empId(employeeDTO.getEmpId())
					.password(employeeDTO.getPassword()).email(employeeDTO.getEmail()).name(employeeDTO.getName())
					.phoneNumber(employeeDTO.getPhoneNumber()).updateDatetime(employeeDTO.getUpdateDatetime())
					.hiredate(employeeDTO.getHiredate()).authority(employeeDTO.getAuthority())
					.vacationCount(employeeDTO.getVacationCount()).manager(toManager(employeeDTO.getManager()))
					.jobTitle(employeeDTO.getJobTitle().toEntity(employeeDTO.getJobTitle()))
					.teamPosition(employeeDTO.getTeamPosition().toEntity(employeeDTO.getTeamPosition()))
					.unit(employeeDTO.getUnit() == null ? null : employeeDTO.getUnit().toFKUnit(employeeDTO.getUnit()))
					.build();
			return employee;
		}
		return toManager(employeeDTO); // 매니저가 없으면 실행
	}

	public Employee toFKManager(EmployeeDTO emp) {
		Employee manager = Employee.builder().empNum(emp.getEmpNum()).build();
		return manager;
	}

	public Employee toManager(EmployeeDTO employeeDTO) {// 빌더에 manager를 빼주었다.
		Employee employee = Employee.builder().authority(employeeDTO.getAuthority()).empNum(employeeDTO.getEmpNum())
				.empId(employeeDTO.getEmpId()).password(employeeDTO.getPassword()).email(employeeDTO.getEmail())
				.name(employeeDTO.getName()).phoneNumber(employeeDTO.getPhoneNumber())
				.updateDatetime(employeeDTO.getUpdateDatetime()).hiredate(employeeDTO.getHiredate())
				.authority(employeeDTO.getAuthority()).vacationCount(employeeDTO.getVacationCount())
				// .manager(toManager(employeeDTO.getManager()))
				.jobTitle(employeeDTO.getJobTitle().toEntity(employeeDTO.getJobTitle()))
				.teamPosition(employeeDTO.getTeamPosition() != null
						? employeeDTO.getTeamPosition().toEntity(employeeDTO.getTeamPosition())
						: null)
				.unit(employeeDTO.getUnit() != null ? employeeDTO.getUnit().toFKUnit(employeeDTO.getUnit()) : null)
				.build();
		return employee;
	}

	// 회원 가입할 때 사용
	public Employee toEmpSignUp(PasswordEncoder passwordEncoder) {
		if (manager == null) {
			return Employee.builder().empId(empId).password(passwordEncoder.encode(password)).name(name)
					.phoneNumber(phoneNumber).email(email).hiredate(hiredate).jobTitle(jobTitle.toEntity(jobTitle))
					.teamPosition(teamPosition.toEntity(teamPosition)).authority(authority !=null ? authority : Authority.ROLE_USER)
					.unit(unit.toFKUnit(unit)).build();
		}
		return Employee.builder().empId(empId).password(passwordEncoder.encode(password)).name(name)
				.phoneNumber(phoneNumber).email(email).hiredate(hiredate).jobTitle(jobTitle.toEntity(jobTitle))
				.teamPosition(teamPosition.toEntity(teamPosition)).manager(toFKManager(manager))
				.authority(authority !=null ? authority : Authority.ROLE_USER).unit(unit.toFKUnit(unit)).build();
	}

	// UsernamePasswordAuthenticationToken를 반환하여 아이디와 비밀번호가 일치하는지 검증하는 로직을 넣을 수 있게
	// 된다.
	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(empId, password);
	}

	// 매니저 추가 삭제시 사용 : 매니저 지정때 사용
	public void setManager2(EmployeeDTO m) {
		manager = m;
	}

}
