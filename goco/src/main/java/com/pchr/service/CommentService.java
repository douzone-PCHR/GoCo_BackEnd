package com.pchr.service;

import com.pchr.dto.CommentDTO;
import com.pchr.dto.PageRequestDTO;
import com.pchr.dto.PageResultDTO;
import com.pchr.entity.Comment;

public interface CommentService {

	PageResultDTO<CommentDTO, Comment> getCommentList(PageRequestDTO prDto,
			Long id);

	void updateComment(Long commentId, CommentDTO commentDto);

}
