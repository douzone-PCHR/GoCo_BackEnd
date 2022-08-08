package com.pchr.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.UnitDTO;
import com.pchr.entity.Authority;
import com.pchr.entity.Employee;
import com.pchr.entity.Resignation;
import com.pchr.entity.Unit;
import com.pchr.repository.EmployeeRepository;
import com.pchr.repository.ResignationRepository;
import com.pchr.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpolyServiceImpl implements EmployeeService{
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ResignationServiceImpl resignationServiceImpl;
	
	
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
	public List<EmployeeDTO> findAll() {
		List<Employee> empList = employeeRepository.findAll();
		List<EmployeeDTO> empDTOList=new ArrayList<EmployeeDTO>();
					empList.forEach((emp)->{
					empDTOList.add(emp.toDTO(emp));
		});	
		return empDTOList;
	}
	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);	
	}
	@Override
	public int deleteByEmail(String email) {
		return employeeRepository.deleteByEmail(email);
	}
	@Override
	public int deleteByEmpId(String empId) {
		return employeeRepository.deleteByempId(empId);
	}
	@Override
	public int deleteByEmpNum(Long empNum) {
		return employeeRepository.deleteByEmpNum(empNum);
	}
	@Override
	public List<Employee> findByManager(Long empNum) {
		return employeeRepository.findByManager(empNum);
	}	
	@Override
	public Optional<Employee> findByEmpNum(Long empNum) {
		return employeeRepository.findByEmpNum(empNum);
	}
	
	   /////////////////////////////////// 이하 회원 정보 수정 //////////////////////////////
	//아이디 체크
	public String idCheck(String info) {
	        if (existsByEmail(info) | existsByEmpId(info)  ) {
	            return "이미 가입되어 있는 유저입니다";
	        }
			return "사용 가능합니다";
	}
	// 내정보 회원탈퇴
	public int delete() {
			String empId = SecurityUtil.getCurrentMemberId();
			if(existsByEmpId(empId)) { // 탈퇴할 회원이 있는지 먼저 확인
				Employee employee = findByEmpId(empId).get();
				Resignation r = employee.toResignation(employee);//퇴사자테이블을위해employee테이블을 Resignation객체로 변환 한다.그 후 저장한다.
				resignationServiceImpl.save(r);
				List<Employee> empManager = findByManager(employee.getEmpNum()); // 외래키 null을 만들기위해 참조하는 모든 값들을 list형태로 불러옴
				empManager.forEach((e)->{
					EmployeeDTO dto = e.toDTO(e);
					dto.setManager(null); // 참조하는 값들을 모두 null로 바꿔준다. 
					save(dto.toEntity(dto));
				});
				return deleteByEmpId(empId);
			}
			return 0;
	}
	
	// 내정보 비번 변경
	public int setPassword(String password) {
			String EmpId = SecurityUtil.getCurrentMemberId();
			EmployeeDTO employeeDTO = findByEmpId(EmpId)
									.map(emp -> emp.toDTO(emp))
									.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
			employeeDTO.setPassword(passwordEncoder.encode((password)));
			save(employeeDTO.toEntity(employeeDTO));
			return 1;
	}
	
	
	// 내정보 반환 하는 메소드
	public EmployeeResponseDTO getMyInfoBySecurity() {
			return findByEmpId(SecurityUtil.getCurrentMemberId())
    		 						.map(EmployeeResponseDTO::of)
    		 						.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
	}
	// 내정보 이메일변경
	public int emailChange(String email) {
			if (existsByEmail(email) ) {
		        throw new RuntimeException("이미 가입되어 있는 이메일 입니다.");
		    }
			String empId = SecurityUtil.getCurrentMemberId();
			 EmployeeDTO employeeDTO = findByEmpId(empId)
					 				.map(emp->emp.toDTO(emp))
									.orElseThrow(()-> new RuntimeException("로그인 유저 정보가 없습니다"));
			employeeDTO.setEmail(email);
			save(employeeDTO.toEntity(employeeDTO));				
			return 1;
	}
	// 내정보 폰번호변경
	public int setPhone(String phoneNumber) {
			String EmpId = SecurityUtil.getCurrentMemberId();
			EmployeeDTO employeeDTO = findByEmpId(EmpId)
									.map(emp-> emp.toDTO(emp))
									.orElseThrow(()-> new RuntimeException("로그인 유저 정보가 없습니다"));
			employeeDTO.setPhoneNumber(phoneNumber);
			save(employeeDTO.toEntity(employeeDTO));			
		return 1;
	}
	// 관리자의 Role 권한변경권
	public int changeRole(Authority authority, String empId) {
			EmployeeDTO employeeDTO = findByEmpId(empId)
									.map(emp->emp.toDTO(emp))
									.orElseThrow(()-> new RuntimeException("로그인 유저 정보가 없습니다"));
			employeeDTO.setAuthority(authority);
			save(employeeDTO.toEntity(employeeDTO));		
			return 1;
	}
 
	//관리자의 휴가 , 메일 , 아이디, 입사일, 이름 , 핸드폰 번호 변경
	public int changeVacation(int number,Long empNum,String data) {
			EmployeeDTO employeeDTO = findByEmpNum(empNum)
									.map(emp->emp.toDTO(emp))
									.orElseThrow(()-> new RuntimeException("회원 정보가 없습니다."));
			switch(number) {
			case 1:// 휴가 변경
				employeeDTO.setVacationCount(Integer.parseInt(data));
				break;
			case 2:// 메일 변경
				employeeDTO.setEmail(data);
				break;
			case 3:// 아이디 변경
				employeeDTO.setEmpId(data);
				break;
			case 4:// 입사일 변경
				SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
				try {
					employeeDTO.setHiredate(fm.parse(data));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case 5:// 이름 변경
				employeeDTO.setName(data);
				break;
			case 6:// 핸드폰 번호 변경
				employeeDTO.setPhoneNumber(data);
				break;
			default:// 에러
					return 0;
			}
			save(employeeDTO.toEntity(employeeDTO));	
			return 1;
	}
	// 관리자의 유저 데이터 삭제
	public int adminDelete(Long empNum) {
		Employee employee = findByEmpNum(empNum)
				.orElseThrow(()-> new RuntimeException("회원 정보가 없습니다."));
		Resignation r = employee.toResignation(employee);//퇴사자테이블을위해employee테이블을 Resignation객체로 변환 한다.그 후 저장한다.
		resignationServiceImpl.save(r);
		List<Employee> empManager = findByManager(employee.getEmpNum()); // 외래키 null을 만들기위해 참조하는 모든 값들을 list형태로 불러옴
		empManager.forEach((e)->{
				EmployeeDTO dto = e.toDTO(e);
				dto.setManager(null); // 참조하는 값들을 모두 null로 바꿔준다. 
				save(dto.toEntity(dto));
		});
		return 	deleteByEmpNum(empNum);
		
	}
	// 관리자의 퇴사자 확인
	public List<Resignation> ResignationAll() {
		return resignationServiceImpl.findAll();
	}


}
