package com.pchr.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pchr.dto.CommentDTO;


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
@JsonInclude
@ToString
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long commentId;

	@Column(name = "comment_content")
	private String commentContent;

	@Column(name = "registered_date")
	private LocalDateTime registeredDate;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@ManyToOne
	@JoinColumn(name = "emp_num", nullable = false)
	private Employee emp;

	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	public CommentDTO toCommentDto(Comment comment) {
		CommentDTO commentDto = CommentDTO.builder()
				.commentId(comment.getCommentId())
				.commentContent(comment.getCommentContent())
				.registeredDate(comment.getRegisteredDate())
				.modifiedDate(comment.getModifiedDate())
				.employeeDto(comment.getEmp().toFKDTO(comment.getEmp()))
				.build();
		return commentDto;
	}

}
