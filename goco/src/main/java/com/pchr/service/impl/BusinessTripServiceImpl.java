package com.pchr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchr.dto.ApproveEnum;
import com.pchr.dto.BusinessTripDTO;
import com.pchr.entity.BusinessTrip;
import com.pchr.repository.BusinessTripRepository;
import com.pchr.service.BusinessTripService;

@Service
public class BusinessTripServiceImpl implements BusinessTripService {

	@Autowired
	private BusinessTripRepository businessRepository;

	// 출장 신청 리스트 (사원)
	@Override
	public List<BusinessTripDTO> getAllBusinessTrip(Long empNum) {
		List<BusinessTrip> businessList = businessRepository.findAllByEmployeeEmpNum(empNum);
		List<BusinessTripDTO> businessListDTO = new ArrayList<BusinessTripDTO>();

		for (BusinessTrip businessEntity : businessList) {
			businessListDTO.add(businessEntity.toBusinessTripDTO(businessEntity));
		}
		return businessListDTO;
	}

	// 출장 신청 리스트 (팀장)
	@Override
	public List<BusinessTripDTO> getAllTeamBusinessTrip(Long unitId) {
		List<BusinessTrip> businessList = businessRepository.findAllByEmployeeUnitUnitId(unitId);
		List<BusinessTripDTO> businessListDTO = new ArrayList<BusinessTripDTO>();

		for (BusinessTrip businessEntity : businessList) {
			businessListDTO.add(businessEntity.toBusinessTripDTO(businessEntity));

		}
		return businessListDTO;
	}

	// 출장 신청
	@Override
	public BusinessTripDTO insertBusinessTrip(BusinessTripDTO businessTripDTO) {
		List<BusinessTripDTO> checkBusinessListDTO = checkBusiness(businessTripDTO);
		if (checkBusinessListDTO.size() == 0) {
			businessRepository.save(businessTripDTO.toBusinessTripEntity(businessTripDTO));
			System.out.println("저장완료");
		} else {
			switch (checkBusinessListDTO.get(0).getApproveYn()) {

			case APPROVE_WAITTING:
				return checkBusinessListDTO.get(0);

			case APPROVE_SUCCESS:
				break;

			default:
				businessRepository.save(businessTripDTO.toBusinessTripEntity(businessTripDTO));
				System.out.println("저장완료");
				break;
			}
		}
		return null;
	}

	// 출장 수정 (사원)
	@Override
	public void updateBusiness(BusinessTripDTO businessTripDTO) {
		if (businessTripDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
			businessRepository.save(businessTripDTO.toBusinessTripEntity(businessTripDTO));
		}

	}

	// 출장 결재 (팀장)
	@Override
	public void approveBusiness(BusinessTripDTO businessTripDTO) {
		if (businessTripDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
			businessRepository.save(businessTripDTO.toBusinessTripEntity(businessTripDTO));

		}
	}

	// check date
	public List<BusinessTripDTO> checkBusiness(BusinessTripDTO businessTripDTO) {
		List<BusinessTripDTO> businessListDTO = new ArrayList<BusinessTripDTO>();
		List<BusinessTrip> businessTrips = businessRepository.checkBusiness(businessTripDTO.getEmployee().getEmpNum(),
				businessTripDTO.getBusinessTripStartDate(), businessTripDTO.getBusinessTripEndDate());

		for (BusinessTrip businessTrip : businessTrips) {
			businessListDTO.add(businessTrip.toBusinessTripDTO(businessTrip));
		}
		System.out.println(businessListDTO);
		return businessListDTO;
	}

}
