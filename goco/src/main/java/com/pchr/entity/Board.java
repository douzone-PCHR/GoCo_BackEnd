package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pchr.dto.BoardDTO;
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
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id",nullable = false)
	private Long boardId;

	@Column(name = "board_title", nullable = false)
	private String boardTitle;

	@Column(name = "board_content")
	private String boardContent;

	@Column(name = "registered_date", updatable = false)
	@CreatedDate
	private LocalDateTime registeredDate;

	@Column(name = "modified_date",insertable = false)
	@LastModifiedDate
	private LocalDateTime modifiedDate;

	@Column(name = "count",insertable = false)
	private int count;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "emp_num", nullable = false)
	private Employee employee;

//	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
//	@JsonIgnore
//	private List<Comment> comments = new ArrayList<Comment>();

	//Entity -> DTO 빌더 (Update /Select 시)
	public BoardDTO toBoardDto(Board board) {
//		List<CommentDTO> commentsDto = new ArrayList<CommentDTO>();
//		//board의 Comment 리스트들을 DTO로 변환
//		for (Comment comment : board.getComments()) {
//			commentsDto.add(comment.toCommentDto(comment));
//		}
		//Builder
		BoardDTO boardDto = BoardDTO.builder()
				.boardId(board.getBoardId())
				.boardTitle(board.getBoardTitle())
				.count(board.getCount())
				.boardContent(board.getBoardContent())
				.employee(board.getEmployee().toFKDTO(board.getEmployee()))
				.registeredDate(board.getRegisteredDate())
//				.comments(commentsDto)
				.modifiedDate(board.getModifiedDate()).build();
		return boardDto;
	}
}
