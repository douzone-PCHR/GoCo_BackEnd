package com.pchr.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.CommuteDTO;
import com.pchr.entity.Commute;
import com.pchr.repository.CommuteRepository;
import com.pchr.service.CommuteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommuteServiceImpl implements CommuteService {

	private final CommuteRepository commuteRepository;

	@Transactional(readOnly = true)
	@Override
	public List<CommuteDTO> findAll() {
		List<CommuteDTO> result = commuteRepository
				.findAllCommute(SecurityUtil.getCurrentMemberId(),
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth(), 0, 0),
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth() + 1, 0, 0))
				.stream().map(commute -> commute.toCommuteDto(commute)).collect(Collectors.toList());

		return result;
	}

// 우리팀원 - 팀원에 대한 오늘 일자중 출근한 사람들 다 가져옴.
	@Transactional(readOnly = true)
	@Override
	public List<CommuteDTO> findById(Long empnum) {
		List<CommuteDTO> findAllByEmployeeEmpNum = commuteRepository.findAllByEmployeeEmpNum(empnum).stream()
				.map(commute -> commute.toCommuteDto(commute)).collect(Collectors.toList());

		return findAllByEmployeeEmpNum;
	}

	public void updateCommute(CommuteDTO commuteDTO) {
		int ClockInhour = commuteDTO.getClockIn().getHour();
		int ClockOuthour = commuteDTO.getClockOut().getHour();

		if (ClockInhour < 9 && ClockOuthour == 0) {
			commuteDTO.setCommuteStatus("2"); // 출근
		} else if ((ClockInhour <= 9 || ClockInhour >= 9) && ClockOuthour != 0) {
			commuteDTO.setCommuteStatus("4"); // 퇴근
		} else if (ClockInhour >= 9 && ClockOuthour == 0) {
			commuteDTO.setCommuteStatus("1"); // 지각
		}

		Commute entity = commuteDTO.toUpdateCommute(commuteDTO);
		commuteRepository.save(entity);

	}

	@Transactional()
	public void deleteCommute(CommuteDTO commuteDTO) {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
				LocalDateTime.now().getDayOfMonth(), 0, 0, 0);

		if (commuteDTO.getCheck() == 1) { // 출근쪽 삭제
			commuteDTO.setClockIn(localDateTime);
		} else if (commuteDTO.getCheck() == 2) { // 퇴근쪽 삭제
			commuteDTO.setClockOut(localDateTime);
		}
		Commute entity = commuteDTO.toUpdateCommute(commuteDTO);
		commuteRepository.save(entity);

	}

	@Transactional(readOnly = true)
	@Override
	public Integer findWorkTime(LocalDateTime startDate, LocalDateTime endDate) {
		String empId = SecurityUtil.getCurrentMemberId();
		Integer findAllCommuteTime = commuteRepository.findAllCommuteTime(startDate, endDate, empId);
		return findAllCommuteTime;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Map<String, Object>> findAllCommuteAndVacationAndBusiness() {
		List<Map<String, Object>> list = commuteRepository.findAllCommuteAndVacationAndBusiness(SecurityUtil.getCurrentMemberId());
		return list;
	}



}
