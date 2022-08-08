package com.pchr.api;

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
@RequestMapping(value = "/board")
@RequiredArgsConstructor
public class BoardRestController {
	private final BoardServiceImpl boardService;

	// 페이지에 따른 데이터 호출
	@GetMapping
	public PageResultDTO<BoardDTO, Board> getBoard(PageRequestDTO prDto) {
		return boardService.getList(prDto);
	}

	// 특정 Title / Content 입력 시 검색
	@GetMapping(value = "/search")
	public PageResultDTO<BoardDTO, Board> getOneBoard(@RequestBody BoardDTO boardDto) {

		return boardService.getSearch(boardDto);
	}

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
		System.out.println(boardDto);
		boardService.insertBoard(boardDto);
	}
}
