package com.pchr.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.CommuteDTO;
import com.pchr.dto.WorkDTO;
import com.pchr.service.impl.CommuteServiceImpl;
import com.pchr.service.impl.WorkServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkRestController {
	
	
	private final WorkServiceImpl workService;
	
	/**
	 * 사원 별 업무 불러오기
	 * @return List<WorkDTO>
	 */

	@GetMapping(value = "/work")
	public List<WorkDTO> findAll(){
		// empno는 spring security 값을 받아온다 
		Long empno = 4L;
		List<WorkDTO> result = null;
		try {
			result = workService.findAllByEmpNo(empno);
			if(result.isEmpty()) {
				new Exception("일정이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 날짜 클릭 후  이벤트 상세 보기
	 * @return List<WorkDTO>
	 */

	@PostMapping(value = "/work/detail")
	public List<WorkDTO> detailFind(@RequestBody LocalDateTime day){
		System.out.println(day);
		Long empno = 4L;
		List<WorkDTO> result = null;
		try {
			result = workService.findAllByDay(day,empno);
			if(result.isEmpty()) {
				System.out.println("일정이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * 업무 추가
	 * @return List<WorkDTO>
	 */

	@PostMapping(value = "/work/{id}")
	public List<WorkDTO> workAdd(Long unitId){
		// unitId는 spring security 값을 받아온다 
		unitId = 2L;
		List<WorkDTO> result = null;
		try {
//			result = workService.findAll(unitId);
			if(result.isEmpty()) {
				new Exception("소속 된 직원이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 업무 수정
	 * @return List<WorkDTO>
	 */

	@PutMapping(value = "/work/{id}")
	public List<WorkDTO> workUpdate(Long unitId){
		// unitId는 spring security 값을 받아온다 
		unitId = 2L;
		List<WorkDTO> result = null;
		try {
//			result = workService.findAll(unitId);
			if(result.isEmpty()) {
				new Exception("소속 된 직원이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 업무 삭제
	 * @return List<WorkDTO>
	 */

	@DeleteMapping(value = "/work/{id}")
	public List<WorkDTO> workDelete(Long unitId){
		// unitId는 spring security 값을 받아온다 
		unitId = 2L;
		List<WorkDTO> result = null;
		try {
//			result = workService.findAll(unitId);
			if(result.isEmpty()) {
				new Exception("소속 된 직원이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
