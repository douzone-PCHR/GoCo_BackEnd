package com.pchr.api;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pchr.dto.BusinessTripDTO;
import com.pchr.service.impl.BusinessTripServiceImpl;

import lombok.RequiredArgsConstructor;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class BusinessTripRestController {

	private final BusinessTripServiceImpl businessTripService;

	// 출장 신청 리스트 (사원)
	@GetMapping(value = "/businesslist/{empNum}")
	public List<BusinessTripDTO> findBusinessTripByEmployeeEmpNum(@PathVariable Long empNum) {
		List<BusinessTripDTO> businessTripList = businessTripService.getAllBusinessTrip(empNum);
		System.out.println(businessTripList);
		return businessTripList;
	}

	// 출장 신청 리스트 (팀장)
	@GetMapping(value = "/businesslist/approve/{unitId}")
	public List<BusinessTripDTO> findBusinessTripByEmployeeUnitId(@PathVariable Long unitId) {
		List<BusinessTripDTO> businessTripList = businessTripService.getAllTeamBusinessTrip(unitId);
		System.out.println(businessTripList);
		return businessTripList;
	}

	// 출장 상세
	@GetMapping(value = "/business/{businessTripId}")
	public BusinessTripDTO findBusinessTripByBusinessTripId(@PathVariable Long businessTripId) {
		return businessTripService.getBusinessTrip(businessTripId);
	}

	// 출장 신청
	@Transactional
	@PostMapping(value = "/business", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<String, List<BusinessTripDTO>> insertBusinessTrip(
			@RequestPart("businessTripDTO") BusinessTripDTO businessTripDTO,
			@RequestPart("file") MultipartFile multipartFile) {
		System.out.println(businessTripDTO);

		return businessTripService.insertBusinessTrip(businessTripDTO, multipartFile);
	}

	// 출장 결재 (팀장)
	@Transactional
	@PutMapping(value = "/business/approve/{businessTripId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void approveBusiness(@RequestBody BusinessTripDTO businessTripDTO) {
		businessTripService.approveBusiness(businessTripDTO);
	}

	// 출장 삭제
	@Transactional
	@PostMapping(value = "/business/del", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void deleteBusiness(@RequestBody BusinessTripDTO businessTripDTO) {
		businessTripService.deleteBusinessTrip(businessTripDTO);
	}
}

//	// check date
//	@GetMapping(value = "/business/check")
//	public List<BusinessTripDTO> checkBusiness(@RequestBody BusinessTripDTO businessTripDTO) {
//
//		return businessTripService.checkBusiness(businessTripDTO);
//	}
