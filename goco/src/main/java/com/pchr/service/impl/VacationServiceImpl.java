package com.pchr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pchr.dto.ApproveEnum;
import com.pchr.dto.FileDTO;
import com.pchr.dto.VacationDTO;
import com.pchr.entity.Vacation;
import com.pchr.repository.VacationRepository;
import com.pchr.service.VacationService;
import com.pchr.util.S3Util;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

@Service
public class VacationServiceImpl implements VacationService {

	@Autowired
	private VacationRepository vacationRepository;

	@Autowired
	private FileServiceImpl fileService;

	// 휴가신청리스트(사원)
	@Override
	public List<VacationDTO> getAllVacation(Long empNum) {

		List<Vacation> vacations = vacationRepository.findAllByEmployeeEmpNum(empNum);
		List<VacationDTO> vacationDTO = new ArrayList<VacationDTO>();

		for (Vacation vacationEntity : vacations) {
			vacationDTO.add(vacationEntity.toVacationDTO(vacationEntity));
		}

		return vacationDTO;
	}

	// 휴가신청리스트(팀장)
	@Override
	public List<VacationDTO> getAllTeamVacation(Long unitId) {
		List<Vacation> vacations = vacationRepository.findAllByEmployeeUnitUnitId(unitId);
		List<VacationDTO> vacationDTO = new ArrayList<VacationDTO>();

		for (Vacation vacationEntity : vacations) {
			vacationDTO.add(vacationEntity.toVacationDTO(vacationEntity));
		}

		return vacationDTO;
	}

	// 휴가 상세
	@Override
	public VacationDTO getVacation(Long vacationId) {
		Vacation vacationEntity = vacationRepository.findVacationByVacationId(vacationId);
		return vacationEntity == null ? null : vacationEntity.toVacationDTO(vacationEntity);
	}

	// 휴가 추가
	@Override
	@Transactional
	public List<VacationDTO> insertVacation(VacationDTO vacationDTO, MultipartFile multipartFile) {
		List<VacationDTO> checkVacationsDTO = checkVacation(vacationDTO);
		if (checkVacationsDTO.size() == 0) {
			try {
				// fileDTO = S3에 Upload
				// newFileDTO = fileService.insertFile 실행 후 DB에 저장된 DTO
				FileDTO fileDTO = S3Util.S3Upload(multipartFile);
				// 파일을 넣었을 때
				if (fileDTO != null) {
					FileDTO newFileDTO = fileService.insertFile(fileDTO);
					System.out.println(newFileDTO.getFileId());
					vacationDTO.setFile(newFileDTO);
				}
				vacationRepository.save(vacationDTO.toVacationEntity(vacationDTO));

			} catch (AwsServiceException | SdkClientException | IOException e) {
				e.printStackTrace();
			}

		}
		// 겹치는 날짜가 존재 할 때
		return checkVacationsDTO;
	}

	// 휴가 결재 (팀장) front에서 처리
	@Override
	@Transactional
	public void approveVacation(VacationDTO vacationDTO) {
		if (vacationDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
			vacationRepository.save(vacationDTO.toVacationEntity(vacationDTO));
		}
	}

	// 휴가 삭제
	@Override
	@Transactional
//	public void deleteVacation(Long vacationId, FileDTO fileDTO, ApproveEnum approveYn) {
	public void deleteVacation(VacationDTO vacationDTO) {
		if (vacationDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {

			S3Util.deleteFile(vacationDTO.getFile().getFileName());
			vacationRepository.deleteById(vacationDTO.getVacationId());
			fileService.deleteFile(vacationDTO.getFile().getFileId());

		}
	}

	// check date
	public List<VacationDTO> checkVacation(VacationDTO vacationDTO) {
		List<VacationDTO> vacationsDTO = new ArrayList<VacationDTO>();
		List<Vacation> vacations = vacationRepository.checkVacation(vacationDTO.getEmployee().getEmpNum(),
				vacationDTO.getVacationStartDate(), vacationDTO.getVacationEndDate());

		for (Vacation vacation : vacations) {
			if (vacation.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
				vacationsDTO.add(vacation.toVacationDTO(vacation));
			}
		}
		System.out.println(vacationsDTO);
		return vacationsDTO;
	}

}
