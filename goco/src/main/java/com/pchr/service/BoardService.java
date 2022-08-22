package com.pchr.service;

import java.util.List;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Board;

public interface BoardService {

//	PageResultDTO<BoardDTO, Board> getNotice(PageRequestDTO pRDTO);
	List<BoardDTO> getNotice();
	List<BoardDTO> getBoard();
//	PageResultDTO<BoardDTO, Board> getBoard(PageRequestDTO pRDTO);
	
//	PageResultDTO<BoardDTO, Board> getSearch(BoardDTO boardDto);
	
	void insertBoard(BoardDTO boardDto);

	void updateBoard(long id, BoardDTO updateBoardDto);

	void removeBoard(long id);

	BoardDTO getBoard(Long id);

}
