package com.pchr.service;

import java.time.LocalDateTime;
import java.util.List;

import com.pchr.dto.WorkDTO;

public interface WorkService {
	
	public List<WorkDTO> findAllByEmpNo(Long empno);
	
	public List<WorkDTO> findAllByDay(LocalDateTime day , Long empno);
}
