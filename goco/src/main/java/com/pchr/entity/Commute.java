package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Transient;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.CommuteDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Commute {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "commute_id")
	private Long commuteId;
	
	@Column(name = "clock_in")
	private LocalDateTime clockIn;
	
	@Column(name = "clock_out")
	private LocalDateTime clockOut;
	
	@Column(name = "commute_status")
	private String commuteStatus;
	
	@ManyToOne
	@JoinColumn(name = "emp_num",nullable = false)
	private Employee employee;
	
	@Column(name= "commute_check")
	private int commuteCheck;
	
	
	//Entity -> DTO 빌더 (Select 시)
		public CommuteDTO toCommuteDto(Commute commute) {
			
			CommuteDTO commuteDTO = CommuteDTO.builder()
					.commuteId(commute.getCommuteId())
					.clockIn(commute.getClockIn())
					.clockOut(commute.getClockOut())
					.commuteStatus(commute.getCommuteStatus())
					.employee(commute.getEmployee().toFKDTO(commute.getEmployee()))
					.commuteCheck(commute.getCommuteCheck())
					.build();
			
			return commuteDTO;
		}
		
		
	
}
