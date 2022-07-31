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
public class BoardDTO {
	
	private Long boardId;
	
	private String boardTitle;
	
	private String boardContent;
	
	private LocalDateTime registeredDate;
	
	private LocalDateTime modifiedDate;
	
	private int count;
	
	private EmployeeDTO employee;
}
