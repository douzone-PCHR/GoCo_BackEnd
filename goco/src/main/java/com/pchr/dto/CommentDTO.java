package com.pchr.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.pchr.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
	
	private Long comment_id;
	
	private String comment_content;
	
	private LocalDateTime registeredDate;
	
	private LocalDateTime modifiedDate;
	
	//https://www.notion.so/b485aa8c64f644af812c9ef8fe2f9cb1 정리
	private BoardDTO board;
	
	private EmployeeDTO employee;
}
