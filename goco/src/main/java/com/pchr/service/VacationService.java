package com.pchr.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.pchr.dto.VacationAndBusinessVO;
import com.pchr.dto.VacationDTO;

public interface VacationService {
	// 휴가 신청 리스트 (사원)
	public List<VacationDTO> getAllVacation(Long empNum);

	// 휴가 신청 리스트 (팀장)
	public List<VacationDTO> getAllTeamVacation(Long unitId);

	// 휴가 상세
	public VacationDTO getVacation(Long vacationId);

	// 휴가 추가
	public Map<String, List<VacationDTO>> insertVacation(VacationDTO vacationDTO, MultipartFile multipartFile);

	// 휴가 결재
	public void approveVacation(VacationDTO vacationDTO);

	// 휴가 삭제
//		public void deleteVacation(Long vacationId, FileDTO fileDTO, ApproveEnum approveYn);
	public void deleteVacation(VacationDTO vacationDTO);
	
	// 휴가 및 출장 검색
	public List<Map<String, Object>> vacationAndBusiness();

	// checkDate
	public Map<String, List<VacationDTO>> checkVacation(VacationDTO vacationDTO);

	// check 잔여일수
	public Float checkVacationCount(Long empNum);

}
