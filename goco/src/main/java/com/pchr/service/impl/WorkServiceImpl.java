package com.pchr.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.pchr.response.Message;
import com.pchr.response.StatusEnum;
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
		CalendarVO calendar = new CalendarVO();
		if (SecurityUtil.getCurrentMemberId().equals(workDTO.getEmployee().getEmpId())) {
			List<Map<String, Object>> findList = workRepository.findAllByDay(startDay, endDay,
					SecurityUtil.getCurrentMemberId(), 0);
			result = calendar.entitytoVO(findList);

		} else {
			List<Map<String, Object>> findList = workRepository.findAllByDay(startDay, endDay,
					workDTO.getEmployee().getEmpId(), 1);
			result = calendar.entitytoVO(findList);
		}

		return result;
	}

	@Transactional
	@Override
	public ResponseEntity<Message> workSave(WorkDTO workDTO) throws DataIntegrityViolationException {
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		Work entity = workDTO.toEntityWork(workDTO);
		workRepository.save(entity);
		message.setStatus(StatusEnum.OK);
		message.setMessage("등록 되었습니다.");
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	@Override
	public WorkDTO findByDayAndId(Long id) {
		WorkDTO workDTO = workRepository.findById(id).map(work -> work.toWorkDto(work)).get();
		return workDTO;
	}

	@Transactional
	@Override
	public ResponseEntity<Message> updateWork(WorkDTO workDTO) {
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		Work entity = workDTO.toEntityWork(workDTO);
		workRepository.save(entity);
		message.setStatus(StatusEnum.OK);
		message.setMessage("수정 되었습니다.");
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	@Transactional
	@Override
	public ResponseEntity<Message> deleteWork(Long id) {
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		workRepository.deleteById(id);
		message.setStatus(StatusEnum.OK);
		message.setMessage("삭제 되었습니다.");
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	public List<EmployeeDTO> findAllWorkByEmp() {
		List<EmployeeDTO> list = employeeRepository.findAllEmp(SecurityUtil.getCurrentMemberId()).stream()
				.map(emp -> emp.toFKDTO(emp)).collect(Collectors.toList());
		return list;
	}

	public List<CalendarVO> findAllCalendar(String empId) {

		CalendarVO calendar = new CalendarVO();
		List<CalendarVO> result = new ArrayList<CalendarVO>();
		String check = "0";
		if (SecurityUtil.getCurrentMemberId().equals(empId)) {
			List<Map<String, Object>> findList = workRepository.findAllCalendarData(SecurityUtil.getCurrentMemberId(), check);
			result = calendar.entityCalendartoVO(findList);
		} else {
			check = "1"; 
			List<Map<String, Object>> findList = workRepository.findAllCalendarData(empId, check);
			result = calendar.entityCalendartoVO(findList);
		}
		return result;
	}

	public void deleteWorkByEmpNum(Long empNum) {
		workRepository.deleteByEmpNum(empNum);
	}

}
