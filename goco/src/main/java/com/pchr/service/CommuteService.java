package com.pchr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.pchr.dto.CommuteDTO;
import com.pchr.entity.Commute;

public interface CommuteService {
	
	public List<CommuteDTO> findAll(Long unitId);
	
	public List<CommuteDTO> findById(Long commuteId);
	
	public void updateCommute(CommuteDTO commuteDTO);
	
	public void deleteCommute(CommuteDTO commuteDTO);
	
}
