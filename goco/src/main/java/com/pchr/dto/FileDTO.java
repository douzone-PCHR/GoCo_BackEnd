package com.pchr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
	
	private Long fileId;
	
	private String uploadPath;
	
	private String fileName;
	
	private BusinessTripDTO businessTrip;
	
	private VacationDTO vacation;
}
