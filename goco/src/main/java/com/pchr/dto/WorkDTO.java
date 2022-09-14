package com.pchr.dto;


import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pchr.entity.Work;

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
	
	private Long workId;
	
	@Size(max = 50)
	@NotEmpty(message = "제목을 입력해주세요. ")
	private String workTitle;
	@Size(max = 255 , message = "내용은 255자 이내로 입력해주세요. ")
	private String workContent;
	
	private Date workStartDate;
	
	private Date workEndDate;

	private boolean workType;
	
	private EmployeeDTO employee;
	
	
	
	// DTO -> Entity 빌더 
	public Work toEntityWork(WorkDTO workDTO) {
		Work work = Work.builder()
				.workId(workDTO.getWorkId())
				.workTitle(workDTO.getWorkTitle())
				.workContent(workDTO.getWorkContent())
				.workStartDate(workDTO.getWorkStartDate())
				.workEndDate(workDTO.getWorkEndDate())
				.workType(workDTO.isWorkType())
				.emp(workDTO.getEmployee().toFKManager(workDTO.getEmployee()))
				.build();
		return work;
	}
	
}


