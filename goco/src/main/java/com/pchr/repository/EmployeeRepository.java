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
    Employee findByNameAndEmail(String name,String email);
    Employee findByEmpIdAndEmail(String empId,String email);
    Optional<Employee> findByEmpNum(Long empNum);
    List<Employee> findAll();
    Employee save(Employee employee);
    int deleteByEmail(String email);
	int deleteByempId(String empId);
	int deleteByEmpNum(Long empNum);
	
	@Query(value = "select * from employee where manager = :empNum",nativeQuery = true)
	List<Employee> findByManager(@Param("empNum") Long empNum);
	
	@Query(value = "update employee set unit_id = NULL where unit_id = :unitId",nativeQuery = true)
	void deleteEmployeeUnitId(@Param("unitId") Long unitId);
  
  	// 휴가 결재 vacationCount 차감
	@Modifying
	@Query(value = "update employee set vacation_count =vacation_count -:count where emp_num = :empNum", nativeQuery = true)
	public void updateVacationCount(@Param("empNum") Long empNum, @Param("count") Long count);


}
