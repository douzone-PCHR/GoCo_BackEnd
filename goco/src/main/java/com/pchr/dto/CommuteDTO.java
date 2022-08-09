package com.pchr.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	private int check;
	// DTO -> Entity 빌더 (Update)
	public Commute toUpdateCommute(CommuteDTO commuteDTO) {
		Commute commute = Commute.builder()
				.commuteId(commuteDTO.getCommuteId())
				.clockIn(commuteDTO.getClockIn())
				.clockOut(commuteDTO.getClockOut())
				.commuteStatus(commuteDTO.getCommuteStatus())
				.employee(commuteDTO.getEmployee().toFKManager(commuteDTO.getEmployee()))
				.build();
		return commute;
	}
	
}
