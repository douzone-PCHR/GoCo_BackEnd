package com.pchr.service.impl;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.pchr.dto.BoardDTO;
import com.pchr.entity.Board;
import com.pchr.repository.BoardRepository;
import com.pchr.repository.CommentRepository;
import com.pchr.service.BoardService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepo;
	private final CommentRepository commentRepo;

	@Override// 모든 공지 출력
	public List<BoardDTO> getNotice(){
		List<BoardDTO> data = boardRepo.findAllByBoardType(0).stream().map(Board::toBoardDto)
                .collect(Collectors.toList());
		Collections.reverse(data);
				return data;
	}
	@Override// 모든 일반 게시글 출력
	public List<BoardDTO> getBoard(){	
		List<BoardDTO> data = boardRepo.findAllByBoardType(1).stream().map(Board::toBoardDto)
	            .collect(Collectors.toList());
		Collections.reverse(data);
	return data;
	}
	@Override// 삭제 Clear
	public void removeBoard(long boardId) {
		if(commentRepo.countComment(boardId)!=0) {
			throw new RuntimeException("게시글 삭제는 댓글이 없는 경우 가능합니다.");
		}
		boardRepo.deleteById(boardId);
	}
	@Override // 게시판 수정
	public void updateBoard(long boardId, BoardDTO updateBoardDto) {
		Board board = boardRepo.findById(boardId).get(); // board에 Entity 가져오기
		BoardDTO boardDto = board.toBoardDto(board); // board DTO 변환

		if (updateBoardDto.getBoardContent() != null) {
			boardDto.setBoardContent(updateBoardDto.getBoardContent());
		}
		if (updateBoardDto.getBoardTitle() != null) {
			boardDto.setBoardTitle(updateBoardDto.getBoardTitle());
		}
		boardRepo.save(boardDto.toUpdateBoard(boardDto));
	}
	@Override // 게시글 추가 , EmpNum도 인자로 받는다  Front : {empNum:n}
	public void insertBoard(BoardDTO boardDto) {
		boardRepo.save(boardDto.toBoard(boardDto));
	}
	@Override	// 상세 조회 Clear
	public BoardDTO getBoard(Long boardId) {
		Board board = boardRepo.findById(boardId).get();
		BoardDTO boardDto = board.toBoardDto(board); // Entity -> Dto 변환
		boardDto.setCount(boardDto.getCount() + 1); // 조회수(Count) 1증가
		Board updateBoard = boardDto.toBoard(boardDto);
		boardRepo.save(updateBoard);
		return boardDto;
	}
}
