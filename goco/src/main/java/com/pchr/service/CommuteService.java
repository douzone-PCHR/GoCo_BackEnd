package com.pchr.service;

import java.util.Optional;

import com.pchr.dto.CommuteDTO;

public interface CommuteService {
	
	public Optional<CommuteDTO> findAll(Long commuteId);
	
	
}
