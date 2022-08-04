package com.pchr.service.impl;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Board;
import com.pchr.repository.BoardRepository;
import com.pchr.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepo;

	// 페이지에 맞는 데이터 출력 Clear
	@Override
	public PageResultDTO<BoardDTO, Board> getList(PageRequestDTO pRDTO) {
		Pageable pageable = pRDTO.getPageable(Sort.by("boardId").ascending());
		Page<Board> result = boardRepo.findAll(pageable);
		Function<Board, BoardDTO> function = (boardEntity -> boardEntity
				.toBoardDto(boardEntity));
		return new PageResultDTO<BoardDTO, Board>(result, function);
	}

	// 특정 데이터 조회 (페이징) Clear
	@Override
	public PageResultDTO<BoardDTO, Board> getSearch(BoardDTO boardDto) {
		// Entity를 담을 List 생성
		Page<Board> boardResults = null;
		// 페이지에 대한 값 전달
		Pageable pageable = new PageRequestDTO()
				.getPageable(Sort.by("boardId").ascending());

		// 중복 검색은 없음.
		if (boardDto.getBoardContent() != null) { // 내용에 대한 검색
			boardResults = boardRepo.findAllByBoardContent(
					boardDto.getBoardContent(), pageable);
		}

		if (boardDto.getBoardTitle() != null) { // 제목에 대한 검색
			boardResults = boardRepo
					.findAllByBoardTitle(boardDto.getBoardTitle(), pageable);
		}

		// Entity를DTO로 변환하는 과정
		Function<Board, BoardDTO> boardsDto = (boardEntity -> boardEntity
				.toBoardDto(boardEntity));

		return new PageResultDTO<BoardDTO, Board>(boardResults, boardsDto);
	}

	// 삭제 Clear
	@Override
	public void removeBoard(long boardId) {
		boardRepo.deleteById(boardId);
	}

// --------------------------------------------------------------------
	// 수정 !!!
	@Override
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

	// 추가 Clear
	// (EmpNum에 대한 값도 받아야함. - Front : {empNum:n})
	@Override
	public void insertBoard(BoardDTO boardDto) {
		boardRepo.save(boardDto.toBoard(boardDto));

	}

	// 상세 조회 Clear
	@Override
	public BoardDTO getBoard(Long boardId) {
		// DB에서 데이터 가져옴. Entity 상태
		Board board = boardRepo.findById(boardId).get();
		BoardDTO boardDto = board.toBoardDto(board); // Entity -> Dto 변환
		boardDto.setCount(boardDto.getCount() + 1); // 조회수(Count) 1증가
		Board updateBoard = boardDto.toBoard(boardDto);
		boardRepo.save(updateBoard);
		return boardDto;
	}
}
