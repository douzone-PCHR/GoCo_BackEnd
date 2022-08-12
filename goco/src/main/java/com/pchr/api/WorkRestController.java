package com.pchr.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping(value = "/work")
	public List<WorkDTO> findAll() {
		List<WorkDTO> result = null;

		try {
			result = workService.findAllByEmpNo();
			if (result.isEmpty()) {
				new Exception("일정이 없습니다.");
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

	@PostMapping(value = "/work/detail")
	public List<WorkDTO> detailFind(@RequestBody LocalDateTime day) {
		List<WorkDTO> result = null;
		try {
			result = workService.findAllByDay(day);
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

	@PostMapping(value = "/work")
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

	@GetMapping(value = "/work/{id}")
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
	 * 날짜 값 없는 업무 리스트 값 출력
	 * 
	 * @return List<WorkDTO>
	 */

	@GetMapping(value = "/work/list")
	public List<WorkDTO> findAllWithoutDate() {
		List<WorkDTO> result = null;
		try {
			result = workService.findAllWithoutDate();
			if (result.isEmpty()) {
				new Exception("일정이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 업무 수정
	 * 
	 * @return boolean
	 */

	@PutMapping(value = "/work")
	public boolean updateWork(@RequestBody WorkDTO workDTO) {

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

	@DeleteMapping(value = "/work/{id}")
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
