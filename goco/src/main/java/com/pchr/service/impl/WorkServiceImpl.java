package com.pchr.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.WorkDTO;
import com.pchr.entity.Employee;
import com.pchr.entity.Work;
import com.pchr.repository.EmployeeRepository;
import com.pchr.repository.WorkRepository;
import com.pchr.service.WorkService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkServiceImpl implements WorkService {

	private final WorkRepository workRepository;
	private final EmployeeRepository employeeRepository;

	@Transactional(readOnly = true)
	@Override
	public List<WorkDTO> findAllByEmpId() {

		List<WorkDTO> list = workRepository.findAllByEmpEmpId(SecurityUtil.getCurrentMemberId()).stream()
				.map(work -> work.toWorkDto(work)).collect(Collectors.toList());
		return list;

	}

	@Transactional(readOnly = true)
	@Override
	public List<WorkDTO> findAllByDay(WorkDTO workDTO) {
		LocalDateTime day = workDTO.getWorkStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); 
		LocalDateTime startDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 0, 0, 0);
		LocalDateTime endDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 23, 59, 0);
		List<WorkDTO> list = null;
	
		if(SecurityUtil.getCurrentMemberId().equals(workDTO.getEmployee().getEmpId())) {
			list = workRepository.findAllByDay(startDay, endDay, SecurityUtil.getCurrentMemberId()).stream()
					.map(work -> work.toWorkDto(work)).collect(Collectors.toList());
		}else {
			list = workRepository.findAllByDay(startDay, endDay, workDTO.getEmployee().getEmpId()).stream()
					.filter(work -> (work.isWorkType() == false))
					.map(work -> work.toWorkDto(work)).collect(Collectors.toList());
		}
		
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


	public List<EmployeeDTO> findAllWorkByEmp() {
		List<EmployeeDTO> list = employeeRepository.findAllEmp(SecurityUtil.getCurrentMemberId()).stream()
				.map(emp -> emp.toFKDTO(emp)).collect(Collectors.toList());
		return list;
	}

	public List<WorkDTO> findAllCalendar(String empId) {
	
		List<WorkDTO> list = null;
		if (SecurityUtil.getCurrentMemberId().equals(empId)) {
			list = workRepository.findAllCalendarData(empId).stream().map(work -> work.toWorkDto(work))
					.collect(Collectors.toList());
		} else {
			list = workRepository.findAllCalendarData(empId).stream().filter(work -> (work.isWorkType() == false))
					.map(work -> work.toWorkDto(work)).collect(Collectors.toList());
		}
		return list;
	}

}
