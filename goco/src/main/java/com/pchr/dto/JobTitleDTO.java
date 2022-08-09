package com.pchr.dto;

import com.pchr.entity.JobTitle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobTitleDTO {
	
	private Long jobTitleId;
	
	private String jobTitleName;
	
	public JobTitle toEntity(JobTitleDTO jobTitleDTO) {
		JobTitle jobTitle = JobTitle.builder()
							.jobTitleId(jobTitleDTO.getJobTitleId())
							.jobTitleName(jobTitleDTO.getJobTitleName())
							.build();
		return jobTitle;
	}
}
