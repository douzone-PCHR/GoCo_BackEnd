package com.pchr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.ApproveEnum;
import com.pchr.dto.FileDTO;
import com.pchr.dto.VacationDTO;
import com.pchr.entity.Vacation;
import com.pchr.repository.EmployeeRepository;
import com.pchr.repository.VacationRepository;
import com.pchr.service.VacationService;
import com.pchr.util.S3Util;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

@RequiredArgsConstructor
@Service
public class VacationServiceImpl implements VacationService {

	private final VacationRepository vacationRepository;

	private final FileServiceImpl fileService;

	private final EmployeeRepository employeeRepository;

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
	public Map<String, List<VacationDTO>> insertVacation(VacationDTO vacationDTO, MultipartFile multipartFile) {
		Map<String, List<VacationDTO>> checkVacationsDTO = checkVacation(vacationDTO);
		if (checkVacationsDTO.get("success").size() == 0) {
			if (checkVacationsDTO.get("waitting").size() == 0) {
				try {
					// 프론트에서 빈파일이 넘어 왔을 때
					if (multipartFile.getSize() != 0) {

						// fileDTO = S3에 Upload된
						// newFileDTO = fileService.insertFile 실행 후 DB에 저장된 DTO
						FileDTO fileDTO = S3Util.S3Upload(multipartFile, "vacation/");
						// 파일을 넣었을 때
						if (fileDTO != null) {
							FileDTO newFileDTO = fileService.insertFile(fileDTO);
							System.out.println(newFileDTO.getFileId());
							vacationDTO.setFile(newFileDTO);
						}
					}
					vacationRepository.save(vacationDTO.toVacationEntity(vacationDTO));

				} catch (AwsServiceException | SdkClientException | IOException e) {
					e.printStackTrace();
				}

			}
		}
		// 겹치는 날짜가 존재 할 때
		return checkVacationsDTO;
	}

	// 휴가 결재 (팀장) front에서 처리
	@Override
	@Transactional
	public void approveVacation(VacationDTO vacationDTO) {
		Float count = 0F;

		switch (vacationDTO.getApproveYn()) {
		case APPROVE_SUCCESS:
			if (vacationDTO.getVacationType().equals("연차")) {

				count = (float) (((vacationDTO.getVacationEndDate().getTime()
						- vacationDTO.getVacationStartDate().getTime())) / (60 * 60 * 24 * 1000));
				System.out.println(count);
				employeeRepository.updateVacationCount(vacationDTO.getEmployee().getEmpNum(), count + 1.0F);
			} else if (vacationDTO.getVacationType().equals("반차")) {
				count = 0.5F;
				employeeRepository.updateVacationCount(vacationDTO.getEmployee().getEmpNum(), count);
			}

			vacationRepository.save(vacationDTO.toVacationEntity(vacationDTO));

			break;
		case APPROVE_CANCEL:
			if (vacationDTO.getVacationType().equals("연차")) {

				count = (float) (((vacationDTO.getVacationStartDate().getTime()
						- vacationDTO.getVacationEndDate().getTime())) / (60 * 60 * 24 * 1000));
				employeeRepository.updateVacationCount(vacationDTO.getEmployee().getEmpNum(), count - 1.0F);
			} else if (vacationDTO.getVacationType().equals("반차")) {

				count = -0.5F;
				employeeRepository.updateVacationCount(vacationDTO.getEmployee().getEmpNum(), count);
			}

			vacationRepository.save(vacationDTO.toVacationEntity(vacationDTO));

			break;
		default:
			vacationRepository.save(vacationDTO.toVacationEntity(vacationDTO));
			break;
		}
	}

	// 휴가 삭제
	@Override
	@Transactional
//	public void deleteVacation(Long vacationId, FileDTO fileDTO, ApproveEnum approveYn) {
	public void deleteVacation(VacationDTO vacationDTO) {
		if (vacationDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
			if (vacationDTO.getFile() != null) {
				S3Util.deleteFile("vacation/" + vacationDTO.getFile().getFileName());
				fileService.deleteFile(vacationDTO.getFile().getFileId());
			}
			vacationRepository.deleteById(vacationDTO.getVacationId());
		}
	}

	// check date
	@Override
	public Map<String, List<VacationDTO>> checkVacation(VacationDTO vacationDTO) {
		List<VacationDTO> waittingDTO = new ArrayList<VacationDTO>();
		List<VacationDTO> successDTO = new ArrayList<VacationDTO>();

		Map<String, List<VacationDTO>> approveMap = new HashMap<String, List<VacationDTO>>();
		List<Vacation> vacations = vacationRepository.checkVacation(vacationDTO.getEmployee().getEmpNum(),
				vacationDTO.getVacationStartDate(), vacationDTO.getVacationEndDate());

		for (Vacation vacation : vacations) {
			if (vacation.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
				waittingDTO.add(vacation.toVacationDTO(vacation));
			} else if (vacation.getApproveYn() == ApproveEnum.APPROVE_SUCCESS) {
				successDTO.add(vacation.toVacationDTO(vacation));
			}
		}
		approveMap.put("waitting", waittingDTO);
		approveMap.put("success", successDTO);

		return approveMap;
	}

	// 잔여 휴가 일수 check
	@Override
	public Float checkVacationCount(Long empNum) {
		return employeeRepository.checkVacationCount(empNum);
	}

	// 매니저 메인페이지 리스트
	public List<Map<String, Object>> vacationAndBusiness() {
		List<Map<String, Object>> findAllApprove = vacationRepository.findAllApprove(SecurityUtil.getCurrentMemberId());
		return findAllApprove;
	}

}
