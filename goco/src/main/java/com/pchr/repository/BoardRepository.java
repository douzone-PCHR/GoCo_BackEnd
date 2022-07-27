package com.pchr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

}
