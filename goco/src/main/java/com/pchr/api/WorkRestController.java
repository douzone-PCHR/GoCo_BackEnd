package com.pchr.api;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pchr.config.SecurityUtil;
import com.pchr.dto.CalendarVO;
import com.pchr.dto.CommuteDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.WorkDTO;
import com.pchr.service.impl.WorkServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkRestController {

	private final WorkServiceImpl workService;

	/**
	 * 사원 별 업무 불러오기
	 * 
	 * @return List<WorkDTO>
	 */

	@GetMapping(value = "/user/work")
	public List<WorkDTO> findAll() {
		List<WorkDTO> result = null;

		try {
			result = workService.findAllByEmpId();
			if (result.isEmpty()) {
				new Exception("일정이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 사원 별 업무 불러오기 달력 
	 * 
	 * @return List<WorkDTO>
	 */

	@GetMapping(value = "/user/work/calendar")
	public List<CalendarVO> findAllCalendar(@RequestParam String empId) {
		List<CalendarVO> result = null;
		
		try {
			result = workService.findAllCalendar(empId);
			if (result.isEmpty()) {
				new Exception("일정이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 직원 별 work list
	 * 
	 * @return List<WorkDTO>
	 */

	@GetMapping(value = "/user/work/emplist")
	public List<EmployeeDTO> findAllWorkByEmp() {
		List<EmployeeDTO> result = null;
		
		try {
			result = workService.findAllWorkByEmp();
			if (result.isEmpty()) {
				new Exception("소속된 직원이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	


	/**
	 * 날짜 클릭 후 날짜 별 이벤트 리스트 출력
	 * 
	 * @return List<WorkDTO>
	 */

	@PostMapping(value = "/user/work/detail")
	public List<WorkDTO> detailFind(@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss") @RequestBody WorkDTO workDTO) {
		List<WorkDTO> result = null;
		try {
			result = workService.findAllByDay(workDTO);
			if (result.isEmpty()) {
				System.out.println("일정이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 업무 추가
	 * 
	 * @return boolean
	 */

	@PostMapping(value = "/user/work",consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean workAdd(@RequestBody WorkDTO workDTO) {
		try {
			workService.workSave(workDTO);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 날짜 클릭 후 이벤트 리스트 상세보기
	 * 
	 * @return List<WorkDTO>
	 */

	@GetMapping(value = "/user/work/{id}")
	public WorkDTO findDetailList(@PathVariable Long id) {

		WorkDTO workDTO = null;

		try {
			workDTO = workService.findByDayAndId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workDTO;
	}

	/**
	 * 업무 수정
	 * 
	 * @return boolean
	 */

	@PutMapping(value = "/user/work")
	public boolean updateWork(@RequestBody WorkDTO workDTO) {
		System.out.println(workDTO);
		try {
			workService.updateWork(workDTO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 업무 삭제
	 * 
	 * @return List<WorkDTO>
	 */

	@DeleteMapping(value = "/user/work/{id}")
	public boolean workDelete(@PathVariable Long id) {
		try {
			workService.deleteWork(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
