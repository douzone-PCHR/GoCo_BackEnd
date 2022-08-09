package com.pchr.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pchr.dto.EmployeeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

@DynamicInsert// emplyee 신규로 컬럼 생성하면서 값 넣을 때 null인건 default로 적용시키기 위한 것 
@EntityListeners(AuditingEntityListener.class)
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_num")
	private Long empNum;

	@Column(name = "emp_id")
	private String empId;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "name")
	private String name;

	@Column(name = "phone_number")
	private String phoneNumber;

	@LastModifiedDate
	@Column(name = "update_datetime",columnDefinition = "datetime default NOW()")
	private Date updateDatetime;

	@CreatedDate
	@Column(name = "hiredate")
	private Date hiredate;

	@Enumerated(EnumType.STRING)
	@Column(name = "authority")
	private Authority authority;
//	@Column(name = "authority",nullable = false)
//	private int authority;

	@Column(name = "vacation_count")
	private int vacationCount;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinColumn(name = "manager", referencedColumnName = "emp_num")
	@JsonIgnore
	private Employee manager;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
	private List<Employee> child;

	@ManyToOne
	@JoinColumn(name = "job_title_id")
	private JobTitle jobTitle;

	@ManyToOne
	@JoinColumn(name = "team_position_id")
	private TeamPosition teamPosition;

	@ManyToOne
	@JoinColumn(name = "unit_id")
	private Unit unit;

	public void setPassword(String password) { // Entity는 setter 지양 => 변경사항
		this.password = password;
	}

	// 매니저가 아닌 사원에 대한 ToDTO
	public EmployeeDTO toDTO(Employee employee) {
		if (employee.getManager() != null) {

			EmployeeDTO employeeDTO = EmployeeDTO.builder().empNum(employee.getEmpNum()).empId(employee.getEmpId())
					.password(employee.getPassword()).email(employee.getEmail()).name(employee.getName())
					.phoneNumber(employee.getPhoneNumber()).updateDatetime(employee.getUpdateDatetime())
					.hiredate(employee.getHiredate()).authority(employee.getAuthority())
					.vacationCount(employee.getVacationCount()).manager(toManagerDTO(employee.getManager()))
					.jobTitle(employee.getJobTitle().toDTO(employee.getJobTitle()))
					.teamPosition(employee.getTeamPosition().toDTO(employee.getTeamPosition()))
					.unit(employee.getUnit().toUnitDTO(unit)).build();
			return employeeDTO;
		}
		return toManagerDTO(employee);
	}

	// Vacation , Board, Comment 등 Entity -> EmpDto
	// 매니저 제외 한 사원에 대한 ToDTO
	// ----------------- FK 걸린 테이블에 대한 toDTO ----------------- //
	public EmployeeDTO toFKDTO(Employee employee) {
		if (employee.getManager() != null) {

			EmployeeDTO employeeDTO = EmployeeDTO.builder().empNum(employee.getEmpNum()).empId(employee.getEmpId())
					// .teamPosition(employee.getTeamPosition().toTeamPositionDto(employee.getTeamPosition))
					// // 나중에 추가해줘야함
					// .unit(employee.getUnit().toDTO(employee.getUnit())) // 나중에 추가해줘야함
					.manager(toManagerDTO(employee.getManager())).name(employee.getName())
					.vacationCount(employee.getVacationCount()).build();
			return employeeDTO;
		}
		return toManagerDTO(employee);
	}

	// 사원 이면서 매니저일때 ToDTO
	public EmployeeDTO toManagerDTO(Employee employee) {
		return EmployeeDTO.builder().empNum(employee.getEmpNum()).empId(employee.getEmpId())
				.password(employee.getPassword()).email(employee.getEmail()).name(employee.getName())
				.phoneNumber(employee.getPhoneNumber()).updateDatetime(employee.getUpdateDatetime())
				.hiredate(employee.getHiredate()).authority(employee.getAuthority())
				.vacationCount(employee.getVacationCount())
				// .manager(toManagerDTO(employee.getManager()))
//				.jobTitle(employee.getJobTitle().toDTO(employee.getJobTitle()))
//				.teamPosition(employee.getTeamPosition().toDTO(employee.getTeamPosition()))
//				.unit(employee.getUnit().toUnitDTO(unit))
				.build();
	}

	public Resignation toResignation(Employee employee) {
		return Resignation.builder().email(employee.getEmail()).name(employee.getName())
				.phoneNumber(employee.getPhoneNumber()).hiredate(employee.getHiredate()).build();
	}

}