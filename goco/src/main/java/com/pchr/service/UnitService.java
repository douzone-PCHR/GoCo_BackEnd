package com.pchr.service;

import java.util.List;

import com.pchr.dto.UnitDTO;

public interface UnitService {
	
	public List<UnitDTO> unitAll();
	public UnitDTO unitInsert(UnitDTO unitDTO);
	public void unitDelete(UnitDTO unitDTO);
	public List<UnitDTO> deptUnit(Long id);
	UnitDTO getOneUnit(UnitDTO unitDto);
	public void unitUpdate(Long unitId, UnitDTO newUnitDTO);
}
