package com.pchr.service;

import java.util.List;
import java.util.Optional;

import com.pchr.dto.EmployeeDTO;
import com.pchr.entity.Employee;

public interface EmployeeService {
	Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmpId(String empId);
    Optional<Employee> findByEmpId(String empId);
    Employee findByNameAndEmail(String name,String email);
    Employee findByEmpIdAndEmail(String empId,String email);
    Optional<Employee> findByEmpNum(Long empNum);
    List<EmployeeDTO> findAll();
    Employee save(Employee employee);
    int deleteByEmail(String email);
	int deleteByEmpId(String empId);
	int deleteByEmpNum(Long empNum);
	public List<Employee> findByManager(Long empNum);
	public List<Employee> findByManager(Long teamPositionId,Long unitId);
}
