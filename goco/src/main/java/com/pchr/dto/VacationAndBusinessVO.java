package com.pchr.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationAndBusinessVO {
	
	private Long id;

	private ApproveEnum approve_yn;

	private String type;

	private EmployeeDTO emp_num;
	
	private Long row_idx;



	
	
}
