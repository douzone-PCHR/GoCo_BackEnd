package com.pchr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pchr.entity.BusinessTrip;

@Repository
public interface BusinessTripRepository extends JpaRepository<BusinessTrip, Long> {

	// 출장 신청 리스트 (사원)
	public List<BusinessTrip> findAllByEmployeeEmpNum(Long empNum);

	// 출장 신청 리스트 (팀장)
	public List<BusinessTrip> findAllByEmployeeUnitUnitId(Long unitId);

	// 출장 상세
	public BusinessTrip findBusinessByBusinessTripId(Long businessTripId);

	// checkBusiness
	@Query(value = "select * from business_trip where emp_num = :empNum and business_trip_start_date <= :endDate and business_trip_end_date >= :startDate", nativeQuery = true)
	public List<BusinessTrip> checkBusinessTrip(@Param("empNum") Long empNum, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

}
