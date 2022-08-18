package com.pchr.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.pchr.dto.CommuteDTO;
import com.pchr.dto.WorkDTO;
import com.pchr.dto.WorkTimeVO;
import com.pchr.entity.Commute;

public interface CommuteService {
	
	public List<CommuteDTO> findAll();
	
	public List<CommuteDTO> findById(Long commuteId);
	
	public void updateCommute(CommuteDTO commuteDTO);
	
	public void deleteCommute(CommuteDTO commuteDTO);
	
	public Integer findWorkTime(LocalDateTime startDate , LocalDateTime endDate);
	
	public List<Map<String, Object>> findAllCommuteAndVacationAndBusiness();
	
}
