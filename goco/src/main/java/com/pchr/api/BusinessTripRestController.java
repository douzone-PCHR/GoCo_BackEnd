package com.pchr.api;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.BusinessTripDTO;
import com.pchr.service.impl.BusinessTripServiceImpl;

@RestController
public class BusinessTripRestController {
	@Autowired
	BusinessTripServiceImpl businessTripService;

	// 출장 신청 리스트 (사원)
	@GetMapping(value = "/business/{empNum}")
	public List<BusinessTripDTO> findBusinessTripByEmployeeEmpNum(@PathVariable Long empNum) {
		List<BusinessTripDTO> businessTripList = businessTripService.getAllBusinessTrip(empNum);
		System.out.println(businessTripList);
		return businessTripList;
	}

	// 출장 신청 리스트 (팀장)
	@GetMapping(value = "/business/approve/{unitId}")
	public List<BusinessTripDTO> findBusinessTripByEmployeeUnitId(@PathVariable Long unitId) {
		List<BusinessTripDTO> businessTripList = businessTripService.getAllTeamBusinessTrip(unitId);
		System.out.println(businessTripList);
		return businessTripList;
	}

	// 출장 신청
	@PostMapping(value = "/business", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void insertBusinessTrip(@RequestBody BusinessTripDTO businessTripDTO) {
		System.out.println(businessTripDTO);
		businessTripService.insertBusinessTrip(businessTripDTO);
	}

	// 출장 수정 (사원) , 출장 취소 approveYn.APPROVE_CANCEL(사원)
	@Transactional
	@PutMapping(value = "/business/{businessTripId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateBusiness(@RequestBody BusinessTripDTO businessTripDTO, @PathVariable Long businessTripId) {
		System.out.println(businessTripDTO);
		businessTripDTO.setBusinessTripId(businessTripId);
		businessTripService.updateBusiness(businessTripDTO);
	}

	// 출장 결재 (팀장)
	@Transactional
	@PutMapping(value = "/business/approve/{businessTripId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void approveBusiness(@RequestBody BusinessTripDTO businessTripDTO) {
		businessTripService.approveBusiness(businessTripDTO);
	}

	// check date
	@GetMapping(value = "/business/check")
	public List<BusinessTripDTO> checkBusiness(@RequestBody BusinessTripDTO businessTripDTO) {

		return businessTripService.checkBusiness(businessTripDTO);
	}

}
