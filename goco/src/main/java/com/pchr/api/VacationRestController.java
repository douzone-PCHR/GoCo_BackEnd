package com.pchr.api;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.VacationDTO;
import com.pchr.dto.VacationEnum;
import com.pchr.service.impl.VacationServiceImpl;

@RestController
public class VacationRestController {
	@Autowired
	VacationServiceImpl vacationService;

	// 휴가신청리스트 (사원)
	@GetMapping(value = "/vacation/{empNum}")
	public List<VacationDTO> findVacationByEmployeeEmpId(@PathVariable Long empNum) {

		List<VacationDTO> vacations = vacationService.getAllVacation(empNum);
		System.out.println(vacations);
		return vacations;
	}

	// 휴가신청리스트 (팀장)
	@GetMapping(value = "/vacation/approve/{unitId}")
	public List<VacationDTO> findVacationByEmployeeUnitId(@PathVariable Long unitId) {
		List<VacationDTO> vacations = vacationService.getAllVacation(unitId);

		return vacations;
	}

	// 휴가 추가
	@PostMapping(value = "/vacation", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void insertVacation(@RequestBody VacationDTO vacationDTO) {
		System.out.println(vacationDTO);

		vacationService.insertVacation(vacationDTO);
	}

	// 휴가 수정 (사원) 검색 front에서 처리
	@Transactional
	@PutMapping(value = "/vacation/{vacationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateVacation(@RequestBody VacationDTO vacationDTO) {
		System.out.println(vacationDTO);
		vacationService.updateVacation(vacationDTO);
	}

//	// 휴가 수정 (사원) 검색 back에서 처리
//	@Transactional
//	@PutMapping(value = "/vacation/{vacationId", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void updateVacation(@PathVariable Long vacationId, @RequestBody VacationDTO vacationDTO) {
//		vacationService.updateVacation(vacationDTO);
//	}

//	// 휴가 결재 (팀장) 검색 back에서 처리 
//	@Transactional
//	@PutMapping(value = "/vacation/approve/{vacationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void approveVacation(@PathVariable Long vacationId, @RequestBody boolean approveYn) {
//		vacationService.approveVacation(vacationId, approveYn);
//	}

	// 휴가 결재 (팀장) 검색 front에서 처리
	@Transactional
	@PutMapping(value = "/vacation/approve/{vacationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void approveVacation(@RequestBody VacationDTO vacationDTO) {
		vacationService.approveVacation(vacationDTO);
	}

	// 휴가 요청 삭제 (사원)
	@DeleteMapping(value = "/vacation/{vacationId}")
	public void deleteVacation(@PathVariable Long vacationId, VacationEnum approveYn) {
		vacationService.deleteVacation(vacationId, approveYn);
	}

	// check date
	@GetMapping(value = "/vacation/check")
	public List<VacationDTO> checkVacation(@RequestBody VacationDTO vacationDTO) {

		return vacationService.checkVacation(vacationDTO);
	}

}
