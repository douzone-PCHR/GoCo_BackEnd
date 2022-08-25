package com.pchr.dto;


import java.util.Date;

import com.pchr.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

	private Long commentId;

	private String commentContent;//

	private Date registeredDate;

	private Date modifiedDate;

	private BoardDTO boardDto;

	private EmployeeDTO employeeDto;
	
	public Comment toComment(CommentDTO commentDto) {
		Comment comment = Comment.builder()	
						.commentId(commentDto.getCommentId())
						.commentContent(commentDto.getCommentContent())
						.registeredDate(commentDto.getRegisteredDate())
						.modifiedDate(commentDto.getModifiedDate())
						.board(commentDto.getBoardDto().toFKBoard(commentDto.getBoardDto()))
						.emp(commentDto.getEmployeeDto().toFKManager(commentDto.getEmployeeDto()))
						.build();
		return comment;
	}
}
