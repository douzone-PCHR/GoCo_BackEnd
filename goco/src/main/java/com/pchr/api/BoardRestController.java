package com.pchr.api;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pchr.dto.BoardDTO;
import com.pchr.service.impl.BoardServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/user/board")
@RequiredArgsConstructor
public class BoardRestController {
	private final BoardServiceImpl boardService;

	/**
	 * 모든 공지 호출
	 * 
	 * @return List<BoardDTO>
	 */
	@GetMapping(value ="/notice")
	public List<BoardDTO> getNotice() {
		return boardService.getNotice();
	}
	/**
	 * 모든 일반 게시판 호출
	 * 
	 * @return List<BoardDTO>
	 */
	@GetMapping
	public List<BoardDTO> getBoard() {
		return boardService.getBoard();
	}
	/**
	 * 상세조회
	 * 
	 * @return BoardDTO
	 */
	@GetMapping(value = "/{boardid}")
	public BoardDTO selectBoard(@PathVariable(name = "boardid") Long id) {
		return boardService.getBoard(id);
	}
	/**
	 * 삭제
	 * 
	 * @return void
	 */
	@DeleteMapping(value = "/{boardid}")
	public void removeBoard(@PathVariable(name = "boardid") Long id) {
		boardService.removeBoard(id);
	}
	/**
	 * 수정
	 * 
	 * @return void
	 */
	@PutMapping(value = "/{boardid}")
	public void updateBoard(@PathVariable(name = "boardid") Long id,@Validated @RequestBody BoardDTO updateBoardDto) {
		boardService.updateBoard(id, updateBoardDto);
	}
	/**
	 * 추가
	 * 
	 * @return void
	 */
	@PostMapping
	public void insertBoard(@Validated @RequestBody BoardDTO boardDto) {
		boardService.insertBoard(boardDto);
	}
}
