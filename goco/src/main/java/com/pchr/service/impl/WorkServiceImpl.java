package com.pchr.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.CalendarVO;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.VacationAndBusinessVO;
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
	public List<CalendarVO> findAllByDay(WorkDTO workDTO) {
		LocalDateTime day = workDTO.getWorkStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime startDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 0, 0, 0);
		LocalDateTime endDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), 23, 59, 0);
		List<CalendarVO> result = new ArrayList<CalendarVO>();
		if (SecurityUtil.getCurrentMemberId().equals(workDTO.getEmployee().getEmpId())) {
			List<Map<String, Object>> findList = workRepository.findAllByDay(startDay, endDay,
					SecurityUtil.getCurrentMemberId(), 0);
			for (int i = 0; i < findList.size(); i++) {
				CalendarVO calenaderVO = CalendarVO.builder().id((BigInteger) findList.get(i).get("id"))
						.title((String) findList.get(i).get("title"))
						.start(((Timestamp) findList.get(i).get("start")).toLocalDateTime())
						.end(((Timestamp) findList.get(i).get("end")).toLocalDateTime())
						.workType((BigDecimal) findList.get(i).get("work_type"))
						.build();

				result.add(calenaderVO);
			}

		} else {
			List<Map<String, Object>> findList = workRepository.findAllByDay(startDay, endDay,
					workDTO.getEmployee().getEmpId(), 1);
			for (int i = 0; i < findList.size(); i++) {
				CalendarVO calenaderVO = CalendarVO.builder().id((BigInteger) findList.get(i).get("id"))
						.title((String) findList.get(i).get("title"))
						.start(((Timestamp) findList.get(i).get("start")).toLocalDateTime())
						.end(((Timestamp) findList.get(i).get("end")).toLocalDateTime())
						.workType((BigDecimal) findList.get(i).get("work_type"))
						.build();
						
				result.add(calenaderVO);
			}
		}

		return result;
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

	public List<CalendarVO> findAllCalendar(String empId) {

		List<CalendarVO> result = new ArrayList<CalendarVO>();
		if (SecurityUtil.getCurrentMemberId().equals(empId)) {
			List<Map<String, Object>> findList = workRepository.findAllCalendarData(SecurityUtil.getCurrentMemberId());
			
			for (int i = 0; i < findList.size(); i++) {
				
				CalendarVO calenaderVO = CalendarVO.builder().id((BigInteger) findList.get(i).get("id"))
						.title((String) findList.get(i).get("title"))
						.start(((Timestamp) findList.get(i).get("start")).toLocalDateTime())
						.end(((Timestamp) findList.get(i).get("end")).toLocalDateTime())
						.workType((BigDecimal) findList.get(i).get("work_type"))
						.build();

				result.add(calenaderVO);
			}

		} else {
			List<Map<String, Object>> findList = workRepository.findAllCalendarData(empId);
			for (int i = 0; i < findList.size(); i++) {
				CalendarVO calenaderVO = CalendarVO.builder().id((BigInteger) findList.get(i).get("id"))
						.title((String) findList.get(i).get("title"))
						.start(((Timestamp) findList.get(i).get("start")).toLocalDateTime())
						.end(((Timestamp) findList.get(i).get("end")).toLocalDateTime())
						.workType((BigDecimal) findList.get(i).get("work_type"))
						.build();

				result.add(calenaderVO);
			}
		}
		return result;
	}

}
