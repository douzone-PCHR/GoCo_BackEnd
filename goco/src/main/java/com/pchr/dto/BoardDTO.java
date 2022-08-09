package com.pchr.dto;

import java.time.LocalDateTime;
	import java.util.ArrayList;
import java.util.List;

import com.pchr.entity.Board;
import com.pchr.entity.Comment;
import com.pchr.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

	private Long boardId;

	private String boardTitle;

	private String boardContent;

	private LocalDateTime registeredDate;

	private LocalDateTime modifiedDate;

	private int count;

	private EmployeeDTO employee;

	private List<CommentDTO> comments = new ArrayList<CommentDTO>();

	// DTO -> Entity 빌더 (Insert)
	public Board toBoard(BoardDTO boardDto) {
		Board board = Board.builder()
				.boardId(boardDto.getBoardId())
				.boardTitle(boardDto.getBoardTitle())
				.boardContent(boardDto.getBoardContent())
				.count(boardDto.getCount())
				.employee(boardDto.getEmployee().toFKManager(boardDto.getEmployee()))
				.build();
		System.out.println(board.getCount());
		return board;
	}

	
	// DTO -> Entity 빌더 (Update)
	public Board toUpdateBoard(BoardDTO boardDto) {

		Board board = Board.builder().boardId(boardDto.getBoardId())
				.boardTitle(boardDto.getBoardTitle())
				.boardContent(boardDto.getBoardContent())
				.count(boardDto.getCount()).employee(boardDto.getEmployee()
						.toFKManager(boardDto.getEmployee()))
				.build();
		return board;
	}
	
	//댓글에 대한 Board 빌더
	public Board toFKBoard(BoardDTO boardDto) {
		Board board = Board.builder().boardId(boardDto.getBoardId()).build();
		
		return board;
	}
}
