package com.pchr.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.pchr.dto.CalendarVO;
import com.pchr.dto.WorkDTO;

public interface WorkService {
	
	public List<WorkDTO> findAllByEmpId();
	
	public List<WorkDTO> findAllByDay(WorkDTO workDTO);
	
	public void workSave(WorkDTO workDTO);
	
	public WorkDTO findByDayAndId(Long id);
	
	public void updateWork(WorkDTO workDTO);
	
	public void deleteWork(Long id);
	
	public List<CalendarVO> findAllCalendar(String empId);
	
	
}
