package com.pchr.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.pchr.response.Message;
import com.pchr.response.StatusEnum;
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
	public List<CalendarVO> detailFind(@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss") @RequestBody WorkDTO workDTO) {
		List<CalendarVO> result = null;
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
	 * @return ResponseEntity<Message>
	 */

	@PostMapping(value = "/user/work",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> workAdd(@Validated @RequestBody WorkDTO workDTO) {
		ResponseEntity<?> workSave = null;
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
		try {
			workSave = workService.workSave(workDTO);
		} catch (Exception e) {
			return new ResponseEntity<WorkDTO>(HttpStatus.OK);
		}
		return workSave;
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
	 * @return ResponseEntity<Message>
	 */

	@PutMapping(value = "/user/work")
	public ResponseEntity<?> updateWork(@Validated @RequestBody WorkDTO workDTO) {
		ResponseEntity<?> workUpdate = null;
		try {
			workUpdate = workService.updateWork(workDTO);
		} catch (Exception e) {
			return new ResponseEntity<WorkDTO>(HttpStatus.BAD_REQUEST);
		}
		return workUpdate;
	}

	/**
	 * 업무 삭제
	 * 
	 * @return ResponseEntity<Message>
	 */

	@DeleteMapping(value = "/user/work/{id}")
	public ResponseEntity<Message> workDelete(@PathVariable Long id) {
		ResponseEntity<Message> deleteWork = null;
		try {
		 deleteWork = workService.deleteWork(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleteWork;
	}

}
