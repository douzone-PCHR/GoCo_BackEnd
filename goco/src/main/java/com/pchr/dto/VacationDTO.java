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

	private ApproveEnum approveYn;

	private String vacationType;

	private String vacationContent;

	private LocalDateTime vacationRequestDate;

	private LocalDateTime vacationApproveDate;

	private EmployeeDTO employee;

	private FileDTO file;

	// toEntity
	public Vacation toVacationEntity(VacationDTO vacationDTO) {
		Vacation vacationEntity = Vacation.builder().vacationId(vacationDTO.getVacationId())
				.vacationStartDate(vacationDTO.getVacationStartDate()).vacationEndDate(vacationDTO.getVacationEndDate())
				.approveYn(vacationDTO.getApproveYn()).vacationType(vacationDTO.getVacationType())
				.vacationContent(vacationDTO.getVacationContent())
				.vacationRequestDate(vacationDTO.getVacationRequestDate())
				.vacationApproveDate(vacationDTO.getVacationApproveDate())
				.file(vacationDTO.getFile() != null ? vacationDTO.getFile().toFileEntity(vacationDTO.getFile()) : null)
				.employee(vacationDTO.getEmployee().toFKManager(vacationDTO.getEmployee())).build();
		return vacationEntity;
	}

}
