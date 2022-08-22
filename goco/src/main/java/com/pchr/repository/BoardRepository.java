package com.pchr.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

//	Page<Board> findAllByBoardContent(String boardContent, Pageable pageable);

//	Page<Board> findAllByBoardTitle(String boardTitle, Pageable pageable);
	
//	Page<Board> findAllByBoardType(int boardType, Pageable pageable);
	
	List<Board> findAllByBoardType(int boardType);
	
	//Board findAllByBoardType(int boardType);

}
