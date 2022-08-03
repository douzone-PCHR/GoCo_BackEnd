package com.pchr.service;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Board;

public interface BoardService {

	PageResultDTO<BoardDTO, Board> getList(PageRequestDTO pRDTO);

	PageResultDTO<BoardDTO, Board> getSearch(BoardDTO boardDto);

	void insertBoard(BoardDTO boardDto);

	void updateBoard(long id, BoardDTO updateBoardDto);

	void removeBoard(long id);

	BoardDTO getBoard(Long id);

}
