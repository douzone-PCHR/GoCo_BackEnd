package com.pchr.service.impl;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.CommentDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Comment;
import com.pchr.repository.CommentRepository;
import com.pchr.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
	private final CommentRepository commentRepo;
	
	//조회
	@Override
	public PageResultDTO<CommentDTO, Comment> getCommentList(PageRequestDTO prDto,Long id){
		Pageable pageable = prDto.getPageable(Sort.by("commentId").ascending());
		
		//BoardId로 Comment조회
		Page<Comment> commentResult = commentRepo.findAllByBoardBoardId(id,pageable);
		Function<Comment, CommentDTO> function = (comment -> comment.toCommentDto(comment));
		return new PageResultDTO<CommentDTO, Comment>(commentResult, function);
	}
	
	// 추가
//	@Override
	public void insertComment(Long id,CommentDTO commentDto,Long empId) {
		BoardDTO boardDto = new BoardDTO();
		EmployeeDTO empDto = new EmployeeDTO();
		boardDto.setBoardId(id);
		empDto.setEmpNum(empId);
		commentDto.setBoardDto(boardDto);
		commentDto.setEmployeeDto(empDto);
		commentRepo.save(commentDto.toComment(commentDto));
	}
	
	//삭제
	public void deleteComment(Long commentId) {
		commentRepo.deleteById(commentId);
	}
}
