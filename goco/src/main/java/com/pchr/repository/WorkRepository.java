package com.pchr.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pchr.dto.WorkDTO;
import com.pchr.entity.Employee;
import com.pchr.entity.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long>{
	
	public List<Work> findAllByEmpEmpId(String empId);
	
	@Query(value = "CALL second_schedule_list(:startDay, :endDay, :empId, :workType )",
			nativeQuery = true)
	public List<Map<String, Object>> findAllByDay(@Param("startDay") LocalDateTime startDay , @Param("endDay") LocalDateTime endDay , @Param("empId") String empId , @Param("workType") int workType);
	
	@Query(value = "select * "
			+ "from work "
			+ "where work_start_date is null  and  work_end_date is null and emp_num = (select emp_num from employee where emp_id = :empId) " ,
			nativeQuery = true)
	public List<Work> findAllWithoutDate(@Param("empId") String empId);
	
	@Query(value = "CALL schedule_list(:PARAM_emp_id  , :PARAM_check)", nativeQuery = true)
	public List<Map<String, Object>> findAllCalendarData(@Param("PARAM_emp_id") String PARAM_emp_id , @Param("PARAM_check") String PARAM_check);

	@Modifying
	@Query(value = "delete from work where emp_num = :empNum", nativeQuery = true)
	void deleteByEmpNum(@Param("empNum") Long empNum);
	
}
