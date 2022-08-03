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

	private Long commentId;

	private String commentContent;

	private LocalDateTime registeredDate;

	private LocalDateTime modifiedDate;

	private BoardDTO boardDto;

	private EmployeeDTO employeeDto;
}
