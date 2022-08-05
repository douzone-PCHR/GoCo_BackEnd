package com.pchr.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.dto.CommuteDTO;
import com.pchr.entity.Commute;
import com.pchr.repository.CommuteRepository;
import com.pchr.service.CommuteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommuteServiceImpl implements CommuteService {

	private final CommuteRepository commuteRepository;

	@Transactional(readOnly = true)
	@Override
	public List<CommuteDTO> findAll(Long unitId) {

		// 원본
		List<CommuteDTO> result = commuteRepository.findAllCommute(unitId, LocalDate.now(), LocalDate.now()).stream()
				.map(commute -> commute.toCommuteDto(commute)).collect(Collectors.toList());

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

		Optional<Commute> updateId = commuteRepository.findById(commuteDTO.getCommuteId());
		if (updateId.isPresent()) {
//			 updateId.set
		}
	}
	
	@Transactional()
	public void deleteCommute(CommuteDTO commuteDTO) {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
		
		if (commuteDTO.getCheck() == 1) { // 출근쪽 삭제
			commuteDTO.setClockIn(localDateTime);
		} else if (commuteDTO.getCheck() == 2) { // 퇴근쪽 삭제
			commuteDTO.setClockOut(localDateTime);
		}
		Commute entity = commuteDTO.toUpdateCommute(commuteDTO);
		commuteRepository.save(entity);

	}

}
