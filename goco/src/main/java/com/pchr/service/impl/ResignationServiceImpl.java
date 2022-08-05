package com.pchr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.entity.Resignation;
import com.pchr.repository.ResignationRepository;
import com.pchr.service.ResignationService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class ResignationServiceImpl implements ResignationService {

	@Autowired
	private ResignationRepository resignationRepository;
	
	@Override
	public List<Resignation> findAll() {
		return resignationRepository.findAll();
	}
	@Override
	public void save(Resignation r) {
		resignationRepository.save(r);
	}

}
