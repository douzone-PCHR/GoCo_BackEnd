package com.pchr.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.CommuteDTO;
import com.pchr.dto.VacationAndBusinessVO;
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
	public VacationAndBusinessVO findWorkTime() {
		 
		Map<String, Object> map = commuteRepository.findAllCommuteTime(SecurityUtil.getCurrentMemberId());
		VacationAndBusinessVO vo = VacationAndBusinessVO.builder()
		.startDate((Date) map.get("start_date"))
		.endDate((Date) map.get("end_date"))
		.commute_work_time((BigDecimal) map.get("commute_work_time"))
		.vacation_count((int) map.get("vacation_count"))
		.build();
		return vo;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Map<String, Object>> findAllCommuteAndVacationAndBusiness() {
		List<Map<String, Object>> list = commuteRepository.findAllCommuteAndVacationAndBusiness(SecurityUtil.getCurrentMemberId());
		return list;
	}

	@Override
	public List<CommuteDTO> findAllCommuteAdmin() {
		List<CommuteDTO> result = commuteRepository
				.findAllCommuteAdmin(
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth(), 0, 0),
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth() + 1, 0, 0))
				.stream().map(commute -> commute.toCommuteDto(commute)).collect(Collectors.toList());
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<VacationAndBusinessVO> findAllMyTeamWorkTime() {
		
		List<Map<String, Object>> findList = commuteRepository.findAllMyTeamWorkTime(SecurityUtil.getCurrentMemberId());
		List<VacationAndBusinessVO> list = new ArrayList<VacationAndBusinessVO>(); 

		for (int i = 0; i < findList.size(); i++) {
			System.out.println(findList.get(i).get("emp_num"));
			VacationAndBusinessVO vo = VacationAndBusinessVO.builder()
			.empNum( (BigInteger) findList.get(i).get("emp_num"))
			.name((String) findList.get(i).get("name"))
			.vacation_approve((String) findList.get(i).get("vacation_approve"))
			.business_approve((String) findList.get(i).get("business_approve"))
			.clock_in((Timestamp) findList.get(i).get("clock_in"))
			.clock_out((Timestamp) findList.get(i).get("clock_out"))
			.vacation_start_date((Timestamp)findList.get(i).get("vacation_start_date"))
			.vacation_end_date((Timestamp)findList.get(i).get("vacation_end_date"))
			.business_trip_start_date((Timestamp)findList.get(i).get("business_trip_start_date"))
			.business_trip_end_date((Timestamp)findList.get(i).get("business_trip_end_date"))
			.commute_work_time((BigDecimal) findList.get(i).get("commute_work_time"))
			.vacation_count((int) findList.get(i).get("vacation_count"))
			.build();
			list.add(vo);
		}
		
	
	
		
		return list;
	}



}
