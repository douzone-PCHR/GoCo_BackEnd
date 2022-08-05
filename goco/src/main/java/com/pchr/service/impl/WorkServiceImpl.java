package com.pchr.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pchr.dto.WorkDTO;
import com.pchr.entity.Work;
import com.pchr.repository.WorkRepository;
import com.pchr.service.WorkService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkServiceImpl implements WorkService{
	
	private final WorkRepository workRepository;
	
	
	public List<WorkDTO> findAllByEmpNo(Long empno) {
		List<WorkDTO> list = workRepository.findAllByEmpEmpNum(empno).stream().map(work -> work.toWorkDto(work)).collect(Collectors.toList());
		return list;
	}


	public List<WorkDTO> findAllByDay(LocalDateTime day , Long empno) {
		LocalDateTime startDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 0, 0, 0);
		LocalDateTime endDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth()+1, 0, 0, 0);
		List<WorkDTO> list = workRepository.findAllByDay(startDay, endDay , empno).stream().map(work -> work.toWorkDto(work)).collect(Collectors.toList());
		return list;
	}

}
