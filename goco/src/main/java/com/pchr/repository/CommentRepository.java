package com.pchr.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Board;
import com.pchr.entity.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findAllByBoardBoardId(Long id);
	Optional<Comment> findByCommentId(Long commentId);
	boolean existsByBoardId(Board boardId);
}
