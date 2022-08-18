package com.pchr.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Vacation;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

	// EmpNum 으로 휴가리스트 조회 (사원)
	public List<Vacation> findAllByEmployeeEmpNum(Long empNum);

	// 휴가리스트 조회 (팀장)
	public List<Vacation> findAllByEmployeeUnitUnitId(Long unitId);

	// 휴가 아이디로 휴가 찾기 (수정 삭제 시)
	public Vacation findVacationByVacationId(Long vacationId);

	// 중복날짜 처리
	// checkVacation
	@Query(value = "select * from vacation where emp_num = :empNum and vacation_start_date <= :endDate and vacation_end_date >= :startDate", nativeQuery = true)
	public List<Vacation> checkVacation(@Param("empNum") Long empNum, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	// 휴가 요청 대기 리스트
	@Query(value = "Call vacation_business_trip(:PARAM_emp_id)", nativeQuery = true)
	public List<Map<String, Object>> findAllApprove(@Param("PARAM_emp_id") String PARAM_emp_id);

}
