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

import com.pchr.dto.CommentDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Comment;
import com.pchr.service.impl.CommentServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/comment")
@RequiredArgsConstructor
public class CommentRestController {
	private final CommentServiceImpl commentService;

	// 전체 데이터 조회
	@GetMapping("/{boardid}")
	public List<CommentDTO> getAllComment(@PathVariable(name = "boardid") Long boardId) {
		return commentService.getCommentList(boardId);
	}

	// 추가
	// BoardId의 값 받아야함
	@PostMapping("/{empid}/{boardid}")
	public void insertComment(@PathVariable("empid") Long empid, @PathVariable("boardid") Long boardid,
			@RequestBody CommentDTO commentDto) {
		commentService.insertComment(boardid, commentDto, empid);
	}

	// 삭제
	@DeleteMapping("/{commentid}")
	public void deleteComment(@PathVariable("commentid") Long commentId) {
		System.out.println("전달된 값 : "+commentId);
		commentService.deleteComment(commentId);
	}
	// 수정
	@PutMapping("/{commentid}")
	public void updateComment(@PathVariable("commentid") Long commentId, @RequestBody CommentDTO commentDto) {
		commentService.updateComment(commentId, commentDto);
	}
}
