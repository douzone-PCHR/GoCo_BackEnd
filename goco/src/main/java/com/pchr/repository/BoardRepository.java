package com.pchr.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.pchr.entity.Board;
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	List<Board> findAllByBoardType(int boardType);

	@Modifying
	@Query(value = "delete from board where emp_num = :empNum", nativeQuery = true)
	void deleteByEmpNum(@Param("empNum") Long empNum);
}
