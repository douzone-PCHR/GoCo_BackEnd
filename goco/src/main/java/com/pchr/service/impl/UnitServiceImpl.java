package com.pchr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchr.dto.UnitDTO;
import com.pchr.entity.Unit;
import com.pchr.repository.UnitRepository;
import com.pchr.service.UnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

	UnitRepository unitrepo;

	@Override
	public List<UnitDTO> unitAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unitInsert(UnitDTO unitDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitUpdate(UnitDTO unitDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UnitDTO> deptUnit(UnitDTO unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unitDelete(UnitDTO unitDTO) {
		// TODO Auto-generated method stub
		
	}
	
}
 