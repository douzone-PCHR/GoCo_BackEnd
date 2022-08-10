package com.pchr.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.WorkDTO;
import com.pchr.entity.Work;
import com.pchr.repository.WorkRepository;
import com.pchr.service.WorkService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkServiceImpl implements WorkService {

	private final WorkRepository workRepository;

	@Transactional(readOnly = true)
	@Override
	public List<WorkDTO> findAllByEmpNo() {
		
		List<WorkDTO> list = workRepository.findAllByEmpEmpId(SecurityUtil.getCurrentMemberId()).stream().map(work -> work.toWorkDto(work))
				.collect(Collectors.toList());
		return list;
	}

	@Transactional(readOnly = true)
	@Override
	public List<WorkDTO> findAllByDay(LocalDateTime day) {
		LocalDateTime startDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 0, 0, 0);
		LocalDateTime endDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth() + 1, 0, 0, 0);
		List<WorkDTO> list = workRepository.findAllByDay(startDay, endDay, SecurityUtil.getCurrentMemberId()).stream()
				.map(work -> work.toWorkDto(work)).collect(Collectors.toList());
		return list;
	}

	@Transactional
	@Override
	public void workSave(WorkDTO workDTO) {
		Work entity = workDTO.toEntityWork(workDTO);
		workRepository.save(entity);
	}

	@Transactional(readOnly = true)
	@Override
	public WorkDTO findByDayAndId(Long id) {
		WorkDTO workDTO = workRepository.findById(id).map(work -> work.toWorkDto(work)).get();
		return workDTO;
	}

	@Transactional
	@Override
	public void updateWork(WorkDTO workDTO) {
		Work entity = workDTO.toEntityWork(workDTO);
		workRepository.save(entity);
	}

	@Transactional
	@Override
	public void deleteWork(Long id) {
		workRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<WorkDTO> findAllWithoutDate() {
//		List<WorkDTO> list = workRepository.findAllWithoutDate(SecurityUtil.getCurrentMemberId()).stream().map(work -> work.toWorkDto(work))
//		.collect(Collectors.toList());
//				List<WorkDTO> list = 
		System.out.println("===========");
		System.out.println(SecurityUtil.getCurrentMemberId());
		System.out.println("===========");
						workRepository.findAllWithoutDate(SecurityUtil.getCurrentMemberId());
		return null;
	}


}
