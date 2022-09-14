package com.pchr.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findAllByBoardBoardId(Long id);
	Optional<Comment> findByCommentId(Long commentId);
	@Query(value = "select count(*) from comment where board_id = :boardId", nativeQuery = true)
	long countComment(@Param("boardId") Long boardId);
	@Modifying
	@Query(value = "delete from comment where emp_num = :empNum", nativeQuery = true)
	void deleteByEmpNum(@Param("empNum") Long empNum);
	
	@Query(value = "select comment_id from comment where board_id = :boardId", nativeQuery = true)
	List<Long> findByBoardId(@Param("boardId") Long boardId);
}
