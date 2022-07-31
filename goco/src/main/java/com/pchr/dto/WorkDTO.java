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
@ToString
public class WorkDTO {
	
	private int workId;
	
	private String workTitle;
	
	private String workContent;
	
	private LocalDateTime workStartDate;
	
	private LocalDateTime workEndDate;
	
	private boolean workType;
	
	private EmployeeDTO employee;
}


