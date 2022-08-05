package com.pchr.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pchr.dto.WorkDTO;
import com.pchr.entity.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long>{
	
	public List<Work> findAllByEmpEmpNum(Long empno);
	
	@Query(value = "select * "
			+ "from work "
			+ "where work_start_date  >= :startDay  and  work_start_date <= :endDay and emp_num = :empno " ,
			nativeQuery = true)
	public List<Work> findAllByDay(@Param("startDay") LocalDateTime startDay , @Param("endDay") LocalDateTime endDay , @Param("empno") Long empno);
}
