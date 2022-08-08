package com.pchr.api;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pchr.dto.VacationDTO;
import com.pchr.service.impl.VacationServiceImpl;

@RestController
public class VacationRestController {
	@Autowired
	VacationServiceImpl vacationService;

	// 휴가신청리스트 (사원)
	@GetMapping(value = "/vacation/{empNum}")
	public List<VacationDTO> findVacationByEmployeeEmpNum(@PathVariable Long empNum) {

		List<VacationDTO> vacations = vacationService.getAllVacation(empNum);
//		System.out.println(vacations);
		return vacations;
	}

	// 휴가신청리스트 (팀장)
	@GetMapping(value = "/vacation/approve/{unitId}")
	public List<VacationDTO> findVacationByEmployeeUnitId(@PathVariable Long unitId) {
		List<VacationDTO> vacations = vacationService.getAllTeamVacation(unitId);

		return vacations;
	}

	// 휴가 상세
	@GetMapping(value = "/vacation/{vacationId}")
	public VacationDTO findVacationByVacationId(@PathVariable Long vacationId) {
		return vacationService.getVacation(vacationId);

	}

	// 휴가 추가, (사원) 검색 front에서 처리
	@Transactional
	@PostMapping(value = "/vacation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<String, List<VacationDTO>> insertVacation(@RequestPart("vacationDTO") VacationDTO vacationDTO,
			@RequestPart("file") MultipartFile multipartFile) {
		System.out.println(vacationDTO);
//		vacationDTO.setVacationId(vacationId);
		return vacationService.insertVacation(vacationDTO, multipartFile);
	}

	// 휴가 결재 (팀장) 검색 front에서 처리
	@Transactional
	@PutMapping(value = "/vacation/approve/{vacationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void approveVacation(@RequestBody VacationDTO vacationDTO) {
		vacationService.approveVacation(vacationDTO);
	}

//	// 휴가 결재 (팀장) 검색 back에서 처리 
//	@Transactional
//	@PutMapping(value = "/vacation/approve/{vacationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void approveVacation(@PathVariable Long vacationId, @RequestBody boolean approveYn) {
//		vacationService.approveVacation(vacationId, approveYn);
//	}

	// 휴가 요청 삭제 (사원)
	@DeleteMapping(value = "/vacation")
//	public void deleteVacation(@PathVariable Long vacationId, FileDTO fileDTO, ApproveEnum approveYn) {
	public void deleteVacation(@RequestBody VacationDTO vacationDTO) {
		System.out.println(vacationDTO.getVacationId());
//		vacationService.deleteVacation(vacationId, fileDTO, approveYn);
		vacationService.deleteVacation(vacationDTO);
	}

	// check date
	@GetMapping(value = "/vacation/check")
	public Map<String, List<VacationDTO>> checkVacation(@RequestBody VacationDTO vacationDTO) {

		return vacationService.checkVacation(vacationDTO);
	}
}
