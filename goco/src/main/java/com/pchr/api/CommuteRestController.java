package com.pchr.api;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.CommuteDTO;
import com.pchr.repository.EmployeeRepository;
import com.pchr.service.CommuteService;
import com.pchr.service.MemberService;
import com.pchr.service.impl.CommuteServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommuteRestController {
	
	
	private final CommuteServiceImpl commuteService;
//	private final 
	/**
	 * 모든 사원 근태 불러오기
	 	(매니저 페이지)
	 * @return Optional<CommuteDTO>
	 */
	@GetMapping(value = "/commute")
	public Optional<CommuteDTO> findAll2(Long commuteId){
		
		commuteService.findAll(commuteId);
		return null;
		
		// unit 아이디 값으로 부서를 구분해주고 거기서 관리자인 사람만 출퇴근 기록을 볼 수 있게끔 
	}
	
	
	/**
	 * 사원 별 근태 불러오기
	 * @return CommuteDTO
	 */
	@GetMapping(value = "/commute/{id}")
	public CommuteDTO findbyId(@PathVariable Long id){
		
		return null;
	}
	
	
	/**
	 * 근태 업데이트
	 * @return boolean
	 */
	@PutMapping(value = "/commute")
	public boolean update(){
		
		return false;
	}
	
	
	/**
	 * 근태 기록 삭제
	 * @return boolean
	 */
	@DeleteMapping(value = "/commute")
	public boolean delete(){
		
		return false;
	}
	
	
	
}
