package com.pchr.dto;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationDTO {
	
	private Long vacationId;
	
	private Date vacationStartDate; 
	
	private Date vacationEndDate; 
	
	private boolean approveYn;
	
	private String vacationType;
	
	private String vacationContent;
	
	private LocalDateTime vacationRequestDate;
	
	private LocalDateTime vacationApproveDate;
	
	private EmployeeDTO employee;
	
}
