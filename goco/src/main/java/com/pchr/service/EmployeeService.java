package com.pchr.service;

import java.util.List;
import java.util.Optional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.UnitDTO;
import com.pchr.entity.Authority;
import com.pchr.entity.Employee;
import com.pchr.entity.Resignation;

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
	public List<Employee> findManagerByEmpNum(Long empNum);
	public List<Employee> findByManager(Long teamPositionId,Long unitId);
	void updateJobTitle(EmployeeDTO emp);
	public List<EmployeeDTO> findAllManager(Long unitId);
	public boolean idCheck(String info);
	public int delete();
	public int setPassword(String password,String password2);
	public EmployeeResponseDTO getMyInfoBySecurity();
	public int emailChange(String email);
	public int setPhone(String phoneNumber);
	public int changeRole(Authority authority, String empId);
	public int changeData(int number,String empNum,String data);
	public int adminDelete(Long empNum);
	public List<Resignation> ResignationAll();
	public int changUnitId(Long empNum, UnitDTO unit);
	public void setUnitID(EmployeeDTO employeeDTO, UnitDTO unit);
	public void setLeader(Employee employee, Long unitId);
	public int changeManager(Long empNum, UnitDTO unit);
	public void LeaderToMember(Long unitId);
	public void MemberToLeader(Employee employee, UnitDTO unit);
	public Employee updateAllEmp(EmployeeDTO emp);
}
