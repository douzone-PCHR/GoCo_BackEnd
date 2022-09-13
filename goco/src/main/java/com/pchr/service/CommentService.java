package com.pchr.service;
import java.util.List;
import com.pchr.dto.CommentDTO;
public interface CommentService {
		List<CommentDTO> getCommentList(Long id);
		void updateComment(Long commentId, CommentDTO commentDto);
		public void insertComment(Long id,CommentDTO commentDto,Long empId);
		public void deleteComment(Long commentId);
		public void deleteCommentByEmpNum(Long empNum);
}
