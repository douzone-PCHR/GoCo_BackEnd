package com.pchr.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.pchr.entity.Vacation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationDTO {

	private Long vacationId;

	private Date vacationStartDate;

	private Date vacationEndDate;

	private VacationEnum approveYn;

	private String vacationType;

	private String vacationContent;

	private LocalDateTime vacationRequestDate;

	private LocalDateTime vacationApproveDate;

	private EmployeeDTO employee;

	// toEntity
	public Vacation toEntity(VacationDTO vacationDTO) {
		Vacation vacationEntity = Vacation.builder().vacationId(vacationDTO.getVacationId())
				.vacationStartDate(vacationDTO.getVacationStartDate()).vacationEndDate(vacationDTO.getVacationEndDate())
				.approveYn(VacationEnum.APPROVE_WAITTING).vacationType(vacationDTO.getVacationType())
				.vacationContent(vacationDTO.getVacationContent())
				.vacationRequestDate(vacationDTO.getVacationRequestDate())
				.vacationApproveDate(vacationDTO.getVacationApproveDate())
				.employee(vacationDTO.getEmployee().toEntity(vacationDTO.getEmployee())).build();
		return vacationEntity;
	}

}
