package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pchr.dto.CommuteDTO;
import com.pchr.dto.WorkDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Work {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "work_id")
	private Long workId;
	@Column(name = "work_title" , length=255 ,nullable = false)
	private String workTitle;
	
	@Column(name = "work_content")
	private String workContent;
	
	@Column(name = "work_start_date")
	private Date workStartDate;
	
	@Column(name = "work_end_date")
	private Date workEndDate;
	
	@Column(name = "work_type")
	private boolean workType;
	
	@ManyToOne
	@JoinColumn(name = "emp_num",nullable = false)
	private Employee emp;
	
	
	//Entity -> DTO 빌더 (Select 시)
	public WorkDTO toWorkDto(Work work) {
		
		WorkDTO workDTO = WorkDTO.builder()
				.workId(work.getWorkId())
				.workTitle(work.getWorkTitle())
				.workContent(work.getWorkContent())
				.workStartDate(work.getWorkStartDate())
				.workEndDate(work.getWorkEndDate())
				.workType(work.isWorkType())
				.employee(work.getEmp().toFKDTO(work.getEmp()))
				.build();
		
		return workDTO;
	}

	
}