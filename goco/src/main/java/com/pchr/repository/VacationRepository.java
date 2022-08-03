package com.pchr.repository;

import java.util.Date;
import java.util.List;

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

	// startdate, enddate로 중복날짜 처리
	// select * from vaction where empno = 직원번호 and startdate <= 2022-07-11 and
	// enddate >= 2022-07-08

	@Query(value = "select * from vacation where emp_num = :empNum and vacation_start_date <= :endDate and vacation_end_date >= :startDate", nativeQuery = true)
	public List<Vacation> checkVacation(@Param("empNum") Long empNum, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

}
