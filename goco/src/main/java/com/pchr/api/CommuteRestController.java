package com.pchr.api;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.CommuteDTO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.VacationAndBusinessVO;
import com.pchr.dto.WorkTimeVO;
import com.pchr.response.Message;
import com.pchr.response.StatusEnum;
import com.pchr.service.impl.CommuteServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommuteRestController {

	private final CommuteServiceImpl commuteService;

	/**
	 * 모든 사원 근태 불러오기 (매니저 페이지)
	 * 
	 * @return List<CommuteDTO>
	 */

	@GetMapping(value = "/manager/commute")
	public List<CommuteDTO> findAll() {
		List<CommuteDTO> result = null;
		try {
			result = commuteService.findAll();
			if (result.isEmpty()) {
				new Exception("소속 된 직원이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 현재 우리팀 근무 현황 (매니저 페이지)
	 * 
	 * @return List<CommuteDTO>
	 */

	@GetMapping(value = "/manager/commute/status")
	public List<Map<String, Object>> findAllCommuteAndVacationAndBusiness() {
		List<Map<String, Object>> result = null;
		try {
			result = commuteService.findAllCommuteAndVacationAndBusiness();
			if (result.isEmpty()) {
				new Exception("소속 된 직원이 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 매니저 페이지 myteam work list
	 * 
	 * @return List<Map<String, Object>>
	 */

	@GetMapping(value = "/manager/commute/myteam")
	public List<VacationAndBusinessVO> findAllMyTeamWorkTime() {
		List<VacationAndBusinessVO> result = null;

		try {
			result = commuteService.findAllMyTeamWorkTime();
			if (result.isEmpty()) {
				new Exception("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 사원 별 근태 불러오기
	 * 
	 * @return CommuteDTO
	 */
	@GetMapping(value = "/commute/{id}")
	public List<CommuteDTO> findbyId(@PathVariable Long id) {
		List<CommuteDTO> findbyComment = null;
		try {
			findbyComment = commuteService.findById(id);
			if (findbyComment.isEmpty()) {
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

	@GetMapping(value = "/user/commute/time")
	public VacationAndBusinessVO workTime() {
		VacationAndBusinessVO result = null;
		try {
			result = commuteService.findWorkTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 근태 업데이트
	 * @return ResponseEntity<Message>
	 */
	@PutMapping(value = "/user/commute")
	public ResponseEntity<Message> update(@RequestBody CommuteDTO commuteDTO) {

		ResponseEntity<Message> updateCommute = null;
		try {
			updateCommute = commuteService.updateCommute(commuteDTO);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateCommute;
	}
//	

	/**
	 * 근태 기록 삭제
	 * 
	 * @return boolean
	 */
	@PutMapping(value = "/commute/delete")
	public boolean delete(@RequestBody CommuteDTO commuteDTO) {

		try {
			commuteService.deleteCommute(commuteDTO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@GetMapping(value = "/admin/commute")
	public List<CommuteDTO> findAllCommuteAdmin() {
		return commuteService.findAllCommuteAdmin();
	}

	/**
	 * 로그인 한 직원 근무 상태
	 * 
	 * @return List<CommuteDTO>
	 */
	@GetMapping(value = "/user/menu/commute")
	public List<CommuteDTO> findMenuCommuteStatus() {
		List<CommuteDTO> result = null;
		try {
			result = commuteService.findMenuCommuteStatus();
		} catch (NullPointerException e) {
			return Collections.emptyList();
		}
		return result;
	}

}
