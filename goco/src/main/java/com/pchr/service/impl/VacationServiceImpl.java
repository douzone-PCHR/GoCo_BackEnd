package com.pchr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchr.dto.ApproveEnum;
import com.pchr.dto.VacationDTO;
import com.pchr.entity.Vacation;
import com.pchr.repository.VacationRepository;
import com.pchr.service.VacationService;

@Service
public class VacationServiceImpl implements VacationService {

	@Autowired
	private VacationRepository vacationRepository;

	// 휴가신청리스트(사원)
	@Override
	public List<VacationDTO> getAllVacation(Long empNum) {

		List<Vacation> vacations = vacationRepository.findAllByEmployeeEmpNum(empNum);
		List<VacationDTO> vacationDTO = new ArrayList<VacationDTO>();

		for (Vacation vacationEntity : vacations) {
			vacationDTO.add(vacationEntity.toDTO(vacationEntity));
		}

		return vacationDTO;
	}

	// 휴가신청리스트(팀장)
	@Override
	public List<VacationDTO> getAllTeamVacation(Long unitId) {
		List<Vacation> vacations = vacationRepository.findAllByEmployeeUnitUnitId(unitId);
		List<VacationDTO> vacationDTO = new ArrayList<VacationDTO>();

		for (Vacation vacationEntity : vacations) {
			vacationDTO.add(vacationEntity.toDTO(vacationEntity));
		}

		return vacationDTO;
	}

	// 휴가 추가
	@Override
	public VacationDTO insertVacation(VacationDTO vacationDTO) {
		List<VacationDTO> checkVacationsDTO = checkVacation(vacationDTO);
		if (checkVacationsDTO.size() == 0) {
			vacationRepository.save(vacationDTO.toEntity(vacationDTO));
		} else {
			switch (checkVacationsDTO.get(0).getApproveYn()) {
			case APPROVE_WAITTING:
				return checkVacationsDTO.get(0);

			case APPROVE_REFUSE:
				vacationRepository.save(vacationDTO.toEntity(vacationDTO));
				break;

			default:

				break;
			}
		}
		return null;
	}

	// 휴가 수정
	@Override
	public void updateVacation(VacationDTO vacationDTO) {
		if (vacationDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
			vacationRepository.save(vacationDTO.toEntity(vacationDTO));
		}

	}
//	// 휴가 수정 back에서 처리
//	@Override
//	public void updateVacation(Long vacationId, VacationDTO vacationDTO) {
//		Vacation vacationFind = vacationRepository.findVacationByVacationId(vacationId);
//		if(!vacationDTO.isApproveYn()) {
//			vacationRepository.save(vacationDTO.toEntity(vacationDTO));
//		}
//	}

	// 휴가 결재 (팀장) back에서 처리
//	@Override
//	public void approveVacation(Long vacationId, boolean approveYn) {
//		Vacation vacationFind = vacationRepository.findVacationByVacationId(vacationId);
//
//		if (vacationFind != null) {
//			VacationDTO vacationDTO = vacationFind.toDTO(vacationFind);
//			vacationDTO.setApproveYn(approveYn);
//
//			vacationRepository.save(vacationDTO.toEntity(vacationDTO));
//		}
//
//	}

	// 휴가 결재 (팀장) front에서 처리
	@Override
	public void approveVacation(VacationDTO vacationDTO) {
		if (vacationDTO.getApproveYn() == ApproveEnum.APPROVE_WAITTING) {
			vacationRepository.save(vacationDTO.toEntity(vacationDTO));
		}
	}

	// 휴가 삭제
	@Override
	public void deleteVacation(Long vacationId, ApproveEnum approveYn) {
		if (approveYn == ApproveEnum.APPROVE_WAITTING) {

			vacationRepository.deleteById(vacationId);
		}
	}

	// check date
	public List<VacationDTO> checkVacation(VacationDTO vacationDTO) {
		List<VacationDTO> vacationsDTO = new ArrayList<VacationDTO>();
		List<Vacation> vacations = vacationRepository.checkVacation(vacationDTO.getEmployee().getEmpNum(),
				vacationDTO.getVacationStartDate(), vacationDTO.getVacationEndDate());

		for (Vacation vacation : vacations) {
			vacationsDTO.add(vacation.toDTO(vacation));
		}
		System.out.println(vacationsDTO);
		return vacationsDTO;
	}
}
