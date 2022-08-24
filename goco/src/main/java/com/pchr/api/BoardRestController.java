package com.pchr.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Board;
import com.pchr.service.impl.BoardServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/user/board")
@RequiredArgsConstructor
public class BoardRestController {
	private final BoardServiceImpl boardService;

	// 모든 공지 호출
//	@GetMapping(value ="/notice")
//	public PageResultDTO<BoardDTO, Board> getNotice(PageRequestDTO prDto) {
//		return boardService.getNotice(prDto);
//	}
	// 모든 공지 호출
	@GetMapping(value ="/notice")
	public List<BoardDTO> getNotice() {
		return boardService.getNotice();
	}
	// 모든 일반 게시판 호출
	@GetMapping
	public List<BoardDTO> getBoard() {
		return boardService.getBoard();
	}
	// 모든 일반 게시판 호출
//	@GetMapping
//	public PageResultDTO<BoardDTO, Board> getBoard(PageRequestDTO prDto) {
//		return boardService.getBoard(prDto);
//	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 특정 Title / Content 입력 시 검색
//	@GetMapping(value = "/search")
//	public PageResultDTO<BoardDTO, Board> getOneBoard(@RequestBody BoardDTO boardDto) {
//		return boardService.getSearch(boardDto);
//	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 상세조회
	@GetMapping(value = "/{boardid}")
	public BoardDTO selectBoard(@PathVariable(name = "boardid") Long id) {
		return boardService.getBoard(id);
	}

	// 삭제
	@DeleteMapping(value = "/{boardid}")
	public void removeBoard(@PathVariable(name = "boardid") Long id) {
		boardService.removeBoard(id);
	}

	// 수정
	@PutMapping(value = "/{boardid}")
	public void updateBoard(@PathVariable(name = "boardid") Long id, @RequestBody BoardDTO updateBoardDto) {
		boardService.updateBoard(id, updateBoardDto);
	}

	// 추가 (empNum에 대한 값 같이 받아야함)
	@PostMapping
	public void insertBoard(@RequestBody BoardDTO boardDto) {
		boardService.insertBoard(boardDto);
	}
}
