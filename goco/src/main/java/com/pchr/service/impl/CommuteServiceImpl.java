package com.pchr.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pchr.dto.CommuteDTO;
import com.pchr.repository.CommuteRepository;
import com.pchr.repository.EmployeeRepository;
import com.pchr.service.CommuteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommuteServiceImpl implements CommuteService{
	
	private final CommuteRepository commuteRepository;
	
	@Override
	public Optional<CommuteDTO> findAll(Long commuteId) {
		return commuteRepository.findByCommuteId(commuteId);
	}

}
