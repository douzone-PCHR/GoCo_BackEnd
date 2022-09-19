package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@DynamicInsert // emplyee 신규로 컬럼 생성하면서 값 넣을 때 null인건 default로 적용시키기 위한 것
@EntityListeners(AuditingEntityListener.class)
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long commentId;

	@Column(name = "comment_content", length=10000)
	private String commentContent;

	@Column(name = "registered_date")	
	@CreatedDate
	private Date registeredDate;

	
	@LastModifiedDate
	@Column(name = "modified_date", columnDefinition = "datetime default NOW()")
	private Date modifiedDate;

	@ManyToOne
	@JoinColumn(name = "emp_num", nullable = false)
	private Employee emp;

	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	public static CommentDTO toCommentDto(Comment comment) {
		CommentDTO commentDto = CommentDTO.builder()
				.commentId(comment.getCommentId())
				.commentContent(comment.getCommentContent())
				.registeredDate(comment.getRegisteredDate())
				.modifiedDate(comment.getModifiedDate())
				.boardDto(comment.getBoard().toBoardDto(comment.getBoard()))
				.employeeDto(comment.getEmp().toFKDTO(comment.getEmp()))
				.build();
		return commentDto;
	}

}
