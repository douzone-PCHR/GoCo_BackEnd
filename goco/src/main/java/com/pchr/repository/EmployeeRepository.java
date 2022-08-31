package com.pchr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Employee;

@Repository
//email로 Employee를 찾는 로직과, email이 존재하는가 판별하는 로직
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Optional<Employee> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByEmpId(String empId);

	Optional<Employee> findByEmpId(String empId);

	Employee findByNameAndEmail(String name, String email);

	Employee findByEmpIdAndEmail(String empId, String email);

	Optional<Employee> findByEmpNum(Long empNum);

	List<Employee> findAll();

	Employee save(Employee employee);

	int deleteByEmail(String email);

	int deleteByempId(String empId);

	int deleteByEmpNum(Long empNum);

	@Query(value = "select * from employee where manager = :empNum", nativeQuery = true)
	List<Employee> findManagerByEmpNum(@Param("empNum") Long empNum);

	@Query(value = "update employee set unit_id = NULL where unit_id = :unitId", nativeQuery = true)
	void deleteEmployeeUnitId(@Param("unitId") Long unitId);

	@Query(value = "select * from employee where team_position_id = :teamPositionId and unit_id = :unitId", nativeQuery = true)
	List<Employee> findByManager(@Param("teamPositionId") Long teamPositionId, @Param("unitId") Long unitId);


	// 매니저 id로 같은 팀원 찾기
	@Query(value = "select  * " 
			+ "from employee " 
			+ " where unit_id = ( " 
			+ "select unit_id "
			+ "from employee "
			+ "where emp_id = :empId )" ,
			
			nativeQuery = true)
	public List<Employee> findAllEmp(@Param("empId") String empId);
	
	// 팀장 찾기
	public Employee findByUnitUnitIdAndTeamPositionTeamPositionId(Long unitId,Long teamPositionId);
  
  
  // 휴가 결재 vacationCount 차감
	@Modifying
	@Query(value = "update employee set vacation_count =vacation_count -:count where emp_num = :empNum", nativeQuery = true)
	public void updateVacationCount(@Param("empNum") Long empNum, @Param("count") Float count);


	// 잔여 휴가 check
	@Query(value = "select vacation_count from employee where emp_num = :empNum ", nativeQuery = true)
	public Float checkVacationCount(@Param("empNum") Long empNum);

//	public List<Employee> findAllByTeamPositionTeamPositionIdAndUnitParentUnitUnitId(Long teamPositionId,Long unitId);
	
	@Query(value ="select * from employee where emp_num in (:empNum)",nativeQuery = true)
	public List<Employee> findAllByEmpNums(@Param("empNum") List<Long> empNum); 
	
	public List<Employee> findAllByUnitUnitId(Long unitId);
	
	// 팀 Id가 n번인 곳에  속해있는 팀원 전체 
	@Query(value = "select * from employee where unit_id in(select unit_id from unit where parent_unit =:unitId)",nativeQuery = true)
	public List<Employee> findAllByUnitParentUnitId(Long unitId);

	// 그 팀에 있는 매니저를 제외한 사원들의 정보 모두 가져오기
	List<Employee> findAllByTeamPositionTeamPositionIdAndUnitUnitId(Long teamId, Long unitId);
	
	//
	public List<Employee> findAllByTeamPositionTeamPositionIdAndUnitParentUnitUnitId(Long teamId,Long unitId);
	
	@Query(value = "select * from employee where manager = :empNum",nativeQuery = true)
	public List<Employee> findAllByManager(Long empNum);
}
