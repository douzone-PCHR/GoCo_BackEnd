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
	
	public JobTitle toJobTitle(JobTitleDTO jobTitleDto) {
		JobTitle jobTitle = JobTitle.builder()
				.jobTitleId(jobTitleDto.getJobTitleId())
				.build();
		return jobTitle;
	}
}
