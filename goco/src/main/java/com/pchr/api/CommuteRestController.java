package com.pchr.api;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.CommuteDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.WorkDTO;
import com.pchr.dto.WorkTimeVO;
import com.pchr.entity.Commute;
import com.pchr.entity.Employee;
import com.pchr.repository.CommuteRepository;
import com.pchr.repository.EmployeeRepository;
import com.pchr.service.EmployeeService;
import com.pchr.service.impl.CommuteServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommuteRestController {
	
	
	private final CommuteServiceImpl commuteService;
	

	/**
	 * 모든 사원 근태 불러오기
	 	(매니저 페이지)
	 * @return List<CommuteDTO>
	 */

	@GetMapping(value = "/commute")
	public List<CommuteDTO> findAll(){
		List<CommuteDTO> result = null;
		try {
			result = commuteService.findAll();
			if(result.isEmpty()) {
				new Exception("소속 된 직원이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * 사원 별 근태 불러오기
	 * @return CommuteDTO
	 */
	@GetMapping(value = "/commute/{id}")
	public List<CommuteDTO> findbyId(@PathVariable Long id){
		List<CommuteDTO> findbyComment = null;
		try {
			 findbyComment = commuteService.findById(id);
			 if(findbyComment.isEmpty()) {
					new Exception("출퇴근 기록이 없습니다.");
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return findbyComment;
	}
	
	/**
	 * 실제 근로 시간 검색 
	 * 
	 * @return List<CommuteDTO>
	 */

	@GetMapping(value = "/commute/time")
	public Integer workTime(@RequestBody WorkTimeVO workTimeVO){
		Integer result = null;
		try {
			result = commuteService.findWorkTime(workTimeVO.getStartDate(),workTimeVO.getEndDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
//	/**
//	 * 근태 업데이트
//	 * @return boolean
//	 */
	@PutMapping(value = "/commute")
	public boolean update(@RequestBody CommuteDTO commuteDTO){
		
		try {
			commuteService.updateCommute(commuteDTO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
//	
	
	/**
	 * 근태 기록 삭제
	 * @return boolean
	 */
	@PutMapping(value = "/commute/delete")
	public boolean delete(@RequestBody CommuteDTO commuteDTO){

		try {
			commuteService.deleteCommute(commuteDTO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
}
