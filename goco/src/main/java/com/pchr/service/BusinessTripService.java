package com.pchr.service;

import java.util.List;

import com.pchr.dto.BusinessTripDTO;

public interface BusinessTripService {

	// 출장 신청 리스트 (사원)
	public List<BusinessTripDTO> getAllBusinessTrip(Long empNum);

	// 출장 신청 리스트 (팀장)
	public List<BusinessTripDTO> getAllTeamBusinessTrip(Long unitId);

	// 출장 신청
	public BusinessTripDTO insertBusinessTrip(BusinessTripDTO businessTripDTO);

	// 출장 수정 (사원)
	public void updateBusiness(BusinessTripDTO businessTripDTO);

	// 출장 결재 (팀장)
	public void approveBusiness(BusinessTripDTO businessTripDTO);

}
