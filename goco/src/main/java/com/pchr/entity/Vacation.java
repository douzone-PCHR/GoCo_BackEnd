package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.pchr.dto.VacationDTO;
import com.pchr.dto.VacationEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vacation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vacation_id")
	private Long vacationId;

	@Column(name = "vacation_start_date")
	private Date vacationStartDate;

	@Column(name = "vacation_end_date")
	private Date vacationEndDate;

	@Column(name = "approve_yn", insertable = false)
	@Enumerated(EnumType.STRING)
	private VacationEnum approveYn;

	@Column(name = "vacation_type")
	private String vacationType;

	@Column(name = "vacation_content")
	private String vacationContent;

	@Column(name = "vacation_request_date", nullable = false)
	private LocalDateTime vacationRequestDate;

	@Column(name = "vacation_approve_date", insertable = false)
	private LocalDateTime vacationApproveDate;

	@ManyToOne
	@JoinColumn(name = "emp_num", nullable = false)
	private Employee employee;

	// toDTO
	public VacationDTO toDTO(Vacation vacationEntity) {
		VacationDTO vacationDTO = VacationDTO.builder().vacationId(vacationEntity.getVacationId())
				.vacationStartDate(vacationEntity.getVacationStartDate())
				.vacationEndDate(vacationEntity.getVacationEndDate()).approveYn(vacationEntity.getApproveYn())
				.vacationType(vacationEntity.getVacationType()).vacationContent(vacationEntity.getVacationContent())
				.vacationRequestDate(vacationEntity.getVacationRequestDate())
				.vacationApproveDate(vacationEntity.getVacationApproveDate())
				.employee(vacationEntity.getEmployee().toFKDTO(vacationEntity.getEmployee())).build();
		return vacationDTO;
	}

}
