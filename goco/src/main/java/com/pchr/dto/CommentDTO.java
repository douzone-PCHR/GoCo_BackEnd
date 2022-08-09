package com.pchr.dto;

import java.time.LocalDateTime;

import com.pchr.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

	private String commentContent;

	private LocalDateTime registeredDate;

	private LocalDateTime modifiedDate;

	private BoardDTO boardDto;

	private EmployeeDTO employeeDto;
	
	public Comment toComment(CommentDTO commentDto) {
		Comment comment = Comment.builder()
				.commentContent(commentDto.getCommentContent())
				.emp(commentDto.getEmployeeDto().toFKManager(commentDto.getEmployeeDto()))
				.board(commentDto.getBoardDto().toFKBoard(commentDto.getBoardDto()))
				.build();
		return comment;
	}
}
