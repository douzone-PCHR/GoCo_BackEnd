package com.pchr.dto;

import java.time.LocalDateTime;

import com.pchr.entity.Employee;

import lombok.Data;

@Data
public class WorkTimeVO {
	
	LocalDateTime startDate;
	
	LocalDateTime endDate;
	
	
}
