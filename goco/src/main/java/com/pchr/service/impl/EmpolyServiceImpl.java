package com.pchr.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchr.entity.Employee;
import com.pchr.repository.EmployeeRepository;
import com.pchr.service.EmployeeService;

@Service
public class EmpolyServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public Optional<Employee> findByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(String email) {
		return employeeRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByEmpId(String empId) {
		return employeeRepository.existsByEmpId(empId);
		
	}

	@Override
	public Optional<Employee> findByEmpId(String empId) {
		return employeeRepository.findByEmpId(empId);
		
	}

	@Override
	public Employee findByNameAndEmail(String name, String email) {
		return employeeRepository.findByNameAndEmail(name, email);
		
	}

	@Override
	public Employee findByEmpIdAndEmail(String empId, String email) {
		return employeeRepository.findByEmpIdAndEmail(empId,email);
		
	}

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
		
	}

	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
		
	}

	@Override
	public int deleteByEmail(String email) {
		return employeeRepository.deleteByEmail(email);
	}
	

}
