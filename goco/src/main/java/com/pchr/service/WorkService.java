package com.pchr.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.pchr.dto.CalendarVO;
import com.pchr.dto.WorkDTO;
import com.pchr.response.Message;

public interface WorkService {
	
	public List<WorkDTO> findAllByEmpId();
	
	public List<CalendarVO> findAllByDay(WorkDTO workDTO);
	
	public ResponseEntity<Message> workSave(WorkDTO workDTO);
	
	public WorkDTO findByDayAndId(Long id);
	
	public ResponseEntity<Message> updateWork(WorkDTO workDTO);
	
	public ResponseEntity<Message> deleteWork(Long id);
	
	public List<CalendarVO> findAllCalendar(String empId);
	
	
}
