package com.pchr.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.pchr.entity.Commute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommuteDTO {
	
	private Long commuteId;
	
	private LocalDateTime clockIn;
	
	private LocalDateTime clockOut;
	
	private String commuteStatus;
	
	private EmployeeDTO employee;
	
//	public Commute toCommute() {
//		return Commute.builder()
//				.commuteId(commuteId);
//		
//	}
	
}
