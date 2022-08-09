package com.pchr.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.pchr.dto.BusinessTripDTO;

public interface BusinessTripService {

	// 출장 신청 리스트 (사원)
	public List<BusinessTripDTO> getAllBusinessTrip(Long empNum);

	// 출장 신청 리스트 (팀장)
	public List<BusinessTripDTO> getAllTeamBusinessTrip(Long unitId);

	// 출장 상세
	public BusinessTripDTO getBusinessTrip(Long businessTripId);

	// 출장 신청
	public Map<String, List<BusinessTripDTO>> insertBusinessTrip(BusinessTripDTO businessTripDTO,
			MultipartFile multipartFile);

	// 출장 결재 (팀장)
	public void approveBusiness(BusinessTripDTO businessTripDTO);

	// 출장 삭제
	public void deleteBusinessTrip(BusinessTripDTO businessTripDTO);

	// check Business
	public Map<String, List<BusinessTripDTO>> checkBusiness(BusinessTripDTO businessTripDTO);

}
