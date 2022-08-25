package com.pchr.service;
import java.util.List;

import com.pchr.dto.BoardDTO;
import com.pchr.dto.CommentDTO;
import com.pchr.dto.EmployeeDTO;
public interface CommentService {
		List<CommentDTO> getCommentList(Long id);
		void updateComment(Long commentId, CommentDTO commentDto);
		public void insertComment(Long id,CommentDTO commentDto,Long empId);
		public void deleteComment(Long commentId);
}
