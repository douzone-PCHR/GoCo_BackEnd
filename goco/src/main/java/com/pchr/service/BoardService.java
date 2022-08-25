package com.pchr.service;

import java.util.List;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Board;

public interface BoardService {
	List<BoardDTO> getNotice();
	List<BoardDTO> getBoard();
	void insertBoard(BoardDTO boardDto);
	void updateBoard(long id, BoardDTO updateBoardDto);
	void removeBoard(long id);
	BoardDTO getBoard(Long id);
}
