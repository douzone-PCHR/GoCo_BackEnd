package com.pchr.service.impl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.pchr.dto.BoardDTO;
import com.pchr.dto.CommentDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.entity.Comment;
import com.pchr.repository.CommentRepository;
import com.pchr.service.CommentService;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
	private final CommentRepository commentRepo;
	
	@Override//조회
	public List<CommentDTO> getCommentList(Long id){
		List<Comment> commentList = commentRepo.findAllByBoardBoardId(id);
		List<CommentDTO> commentDto = commentList.stream()
										.map(Comment::toCommentDto)
										.collect(Collectors.toList());
		return commentDto;
	}
	@Override// 추가
	public void insertComment(Long id,CommentDTO commentDto,Long empId) {
		BoardDTO boardDto = new BoardDTO();
		EmployeeDTO empDto = new EmployeeDTO();
		boardDto.setBoardId(id);
		empDto.setEmpNum(empId);
		commentDto.setBoardDto(boardDto);
		commentDto.setEmployeeDto(empDto);
		commentRepo.save(commentDto.toComment(commentDto));
	}
	@Override//삭제
	public void deleteComment(Long commentId) {
		commentRepo.deleteById(commentId);
	}
	@Override//댓글수정
	public void updateComment(Long commentId,CommentDTO commentDto) {
		CommentDTO commentdto = commentRepo.findByCommentId(commentId)
									.map(comment->comment.toCommentDto(comment))
									.orElseThrow(()-> new RuntimeException("로그인 유저 정보가 없습니다"));
		commentdto.setCommentContent(commentDto.getCommentContent());
		Comment com = commentdto.toComment(commentdto);
		commentRepo.save(com);
	}
}
