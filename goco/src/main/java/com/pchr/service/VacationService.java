package com.pchr.service;

import java.util.List;

import com.pchr.dto.VacationDTO;
import com.pchr.dto.ApproveEnum;

public interface VacationService {
	// 휴가 신청 리스트 (사원)
	public List<VacationDTO> getAllVacation(Long empNum);

	// 휴가 신청 리스트 (팀장)
	public List<VacationDTO> getAllTeamVacation(Long unitId);

	// 휴가 추가
	public VacationDTO insertVacation(VacationDTO vacationDTO);

	// 휴가 수정
//		public void updateVacation(Long vacationId, VacationDTO vacationDTO);
	public void updateVacation(VacationDTO vacationDTO);

	// 휴가 결재
//		public void approveVacation(Long vacationId, boolean approveYn);
	public void approveVacation(VacationDTO vacationDTO);

	// 휴가 삭제
	void deleteVacation(Long vacationId, ApproveEnum approveYn);

}
