package com.pchr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.ApproveEnum;
import com.pchr.dto.BusinessTripDTO;
import com.pchr.dto.FileDTO;
import com.pchr.dto.VacationDTO;
import com.pchr.entity.BusinessTrip;
import com.pchr.repository.BusinessTripRepository;
import com.pchr.service.BusinessTripService;
import com.pchr.util.S3Util;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

@Service
@RequiredArgsConstructor
public class BusinessTripServiceImpl implements BusinessTripService {

	private final BusinessTripRepository businessRepository;

	private final FileServiceImpl fileService;

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

	// 출장 상세
	@Override
	public BusinessTripDTO getBusinessTrip(Long businessTripId) {
		BusinessTrip businessEntity = businessRepository.findBusinessByBusinessTripId(businessTripId);
		return businessEntity == null ? null : businessEntity.toBusinessTripDTO(businessEntity);
	}

	// 출장 신청
	@Override
	@Transactional
	public Map<String, List<BusinessTripDTO>> insertBusinessTrip(BusinessTripDTO businessTripDTO,
			MultipartFile multipartFile) {
		Map<String, List<BusinessTripDTO>> checkBusinessTripsDTO = checkBusiness(businessTripDTO);
		if (checkBusinessTripsDTO.get("success").size() == 0) {
			if (checkBusinessTripsDTO.get("waitting").size() == 0) {
				try {
					// 프론트에서 빈파일이 넘어 왔을 때
					if (multipartFile.getSize() != 0) {
						// fileDTO = S3에 Upload된
						// newFileDTO = fileService.insertFile 실행 후 DB에 저장된 DTO
						FileDTO fileDTO = S3Util.S3Upload(multipartFile, "business/");
						// 파일을 넣었을 때
						if (fileDTO != null) {
							FileDTO newFileDTO = fileService.insertFile(fileDTO);
							System.out.println(newFileDTO.getFileId());
							businessTripDTO.setFile(newFileDTO);
						}
					}
					businessRepository.save(businessTripDTO.toBusinessTripEntity(businessTripDTO));

				} catch (AwsServiceException | SdkClientException | IOException e) {
					e.printStackTrace();
				}

				// 겹치는 날짜가 존재 할 때

			}
		}
		return checkBusinessTripsDTO;
	}

	// 출장 결재 (팀장)
	@Override
	public void approveBusiness(BusinessTripDTO businessTripDTO) {
		businessRepository.save(businessTripDTO.toBusinessTripEntity(businessTripDTO));

	}

	// 출장 삭제
	@Override
	@Transactional
	public void deleteBusinessTrip(BusinessTripDTO businessTripDTO) {
		if (businessTripDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {

			if (businessTripDTO.getFile() != null) {
				S3Util.deleteFile("business/" + businessTripDTO.getFile().getFileName());
				fileService.deleteFile(businessTripDTO.getFile().getFileId());
			}
			businessRepository.deleteById(businessTripDTO.getBusinessTripId());
		}
	}

	// check date
	@Override
	public Map<String, List<BusinessTripDTO>> checkBusiness(BusinessTripDTO businessTripDTO) {
		List<BusinessTripDTO> waittingDTO = new ArrayList<BusinessTripDTO>();
		List<BusinessTripDTO> successDTO = new ArrayList<BusinessTripDTO>();

		Map<String, List<BusinessTripDTO>> approveMap = new HashMap<String, List<BusinessTripDTO>>();
		List<BusinessTrip> businessTrips = businessRepository.checkBusinessTrip(
				businessTripDTO.getEmployee().getEmpNum(), businessTripDTO.getBusinessTripStartDate(),
				businessTripDTO.getBusinessTripEndDate());

		for (BusinessTrip businessTrip : businessTrips) {
			if (businessTrip.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
				waittingDTO.add(businessTrip.toBusinessTripDTO(businessTrip));
			} else if (businessTrip.getApproveYn() == ApproveEnum.APPROVE_SUCCESS) {
				successDTO.add(businessTrip.toBusinessTripDTO(businessTrip));
			}

		}
		approveMap.put("waitting", waittingDTO);
		approveMap.put("success", successDTO);

		return approveMap;
	}
	
	public List<BusinessTripDTO> vacationAndBusiness() {
		List<BusinessTripDTO> businessTripList = businessRepository.findAllApprove(SecurityUtil.getCurrentMemberId()).stream().map(business -> business.toBusinessTripDTO(business)).collect(Collectors.toList()); 
		return businessTripList;
	}

}
