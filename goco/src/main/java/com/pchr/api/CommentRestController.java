package com.pchr.api;

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

	// 조회
	// boardId를 같이 받아야함.
	@GetMapping("/{boardid}")
	public PageResultDTO<CommentDTO, Comment> getAllComment(@PathVariable(name = "boardid") Long boardId,
			@RequestBody PageRequestDTO prDto) {
		prDto.setSize(5); // 댓글 기본 갯수 5개 설정
		return commentService.getCommentList(prDto, boardId);
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
		commentService.deleteComment(commentId);
	}

	@PutMapping("/{commentid}")
	public void updateComment(@PathVariable("commentid") Long commentId, @RequestBody CommentDTO commentDto) {
		commentService.updateComment(commentId, commentDto);
	}
}
