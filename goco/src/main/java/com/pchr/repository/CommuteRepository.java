package com.pchr.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pchr.dto.CommuteDTO;
import com.pchr.entity.Commute;
import com.pchr.entity.Employee;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, Long>{
	
	
//	Optional<CommuteDTO> findByCommuteId(Long commuteId);
	
//	List<Commute> findAllByEmployeeEmpNum(Long empNum);
	
	
//  @Query(value = "select * from employee where unit_id =\\: unit_id" , nativeQuery = true )
//  Optional<List<Employee>> findAllByUnit(@Param("unit_id") Long unit_id);
//  
//  
	@Query(value = "select * "
			+ "from commute c "
			+ "left join employee e "
			+ "on e.emp_num = c.emp_num "
			+ "where clock_in >= :clock_in "
			+ "and clock_out >= :clock_out "
			+ "and e.unit_id = :unit_id ",

			nativeQuery = true)
	public List<Commute> findAllCommute(@Param("unit_id") Long unit_id, @Param("clock_in") LocalDate clock_in,
			@Param("clock_out") LocalDate clock_out);
//   Optional<List<Employee>> findAllByUnitUnitId(Long unitId);
	
	public List<Commute> findAllByEmployeeEmpNum(Long empnum);
//	public List<Commute> findAllByEmployeeEmpId(Long empnum);
//	findAllByEmployeeEmpId
	
}
