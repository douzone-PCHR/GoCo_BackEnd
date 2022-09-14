package com.pchr.service;
import java.util.List;
import com.pchr.dto.BoardDTO;

public interface BoardService {
	List<BoardDTO> getNotice();
	List<BoardDTO> getBoard();
	void insertBoard(BoardDTO boardDto);
	void updateBoard(long id, BoardDTO updateBoardDto);
	void removeBoard(long id);
	BoardDTO getBoard(Long id);
	public void deleteBoardByEmpNum(Long empNum);
	public List<Long> findAllByBoardId(Long empNum);
}
